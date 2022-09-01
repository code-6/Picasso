package org.novinomad.picasso.domain.entities.impl;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.commons.IRange;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.domain.entities.base.AbstractEntity;
import org.novinomad.picasso.exceptions.BindException;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import static org.novinomad.picasso.commons.utils.CommonDateUtils.COMMON;
import static org.novinomad.picasso.commons.utils.CommonDateUtils.ISO_8601;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(indexes = @Index(columnList = "tour_id,employee_id,startDate,endDate", unique = true))
public class TourBind extends AbstractEntity implements IRange {

    @OneToOne
    Employee employee;

    @OneToOne
    Tour tour;

    @DateTimeFormat(pattern = COMMON)
    @Column(nullable = false)
    LocalDateTime startDate;

    @DateTimeFormat(pattern = COMMON)
    @Column(nullable = false)
    LocalDateTime endDate;

    public TourBind(@NotNull Employee employee,
                    @NotNull Tour tour,
                    @NotNull LocalDateTimeRange dateTimeRange) throws BindException {
        this.employee = employee;
        this.tour = tour;
        this.startDate = dateTimeRange.getStartDate();
        this.endDate = dateTimeRange.getEndDate();

        if (isOutOfTourDateRange())
            throw new BindException(employee, tour, getDateRange(), "out of tour date range");
    }

    public TourBind(Long id,
                    @NotNull Employee employee,
                    @NotNull Tour tour,
                    @NotNull LocalDateTimeRange dateTimeRange) throws BindException {
        this(employee, tour, dateTimeRange);
        this.id = id;

        if (isOutOfTourDateRange())
            throw new BindException(employee, tour, getDateRange(), "out of tour date range");
    }

    public TourBind(@NotNull Employee employee,
                    @NotNull Tour tour,
                    @NotNull LocalDateTime startDate,
                    @NotNull LocalDateTime endDate) throws BindException {
        this(employee, tour, new LocalDateTimeRange(startDate, endDate));
    }

    public TourBind withId(Long id) {
        setId(id);
        return this;
    }

    boolean isOutOfTourDateRange() {
        LocalDateTime tourStartDate = tour.getStartDate();
        LocalDateTime tourEndDate = tour.getEndDate();

        return startDate.isAfter(tourEndDate) || endDate.isBefore(tourStartDate);
    }

    //region equals, hashCode, toString

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TourBind tourBind = (TourBind) o;
        return tour.equals(tourBind.tour) && employee.equals(tourBind.employee) && startDate.equals(tourBind.startDate) && endDate.equals(tourBind.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tour, employee, startDate, endDate);
    }

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(COMMON, Locale.ENGLISH);
        return super.toString().replace("}", "") +
                ", tour=" + tour +
                ", employee=" + employee +
                ", startDate=" + startDate.format(dateTimeFormatter) +
                ", endDate=" + endDate.format(dateTimeFormatter) +
                '}';
    }

    @Override
    public String toStringFull() {
        return toString();
    }

    //endregion
}
