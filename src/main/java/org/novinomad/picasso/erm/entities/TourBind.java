package org.novinomad.picasso.erm.entities;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.commons.IRange;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.erm.entities.base.AbstractEntity;
import org.novinomad.picasso.commons.exceptions.BindException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import static org.novinomad.picasso.commons.utils.CommonDateUtils.UI_DATE_TIME_NO_SEC;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(indexes = @Index(columnList = "tour_id,tour_participant_id,startDate,endDate", unique = true))
public class TourBind extends AbstractEntity implements IRange {

    @OneToOne
    TourParticipant tourParticipant;

    @OneToOne
    Tour tour;

    @Column(nullable = false)
    LocalDateTime startDate;

    
    @Column(nullable = false)
    LocalDateTime endDate;

    public TourBind(@NotNull TourParticipant tourParticipant,
                    @NotNull Tour tour,
                    @NotNull IRange dateTimeRange) throws BindException {
        this.tourParticipant = tourParticipant;
        this.tour = tour;
        this.startDate = dateTimeRange.getStartDate();
        this.endDate = dateTimeRange.getEndDate();

        if (isOutOfTourDateRange())
            throw new BindException(tourParticipant, tour, new LocalDateTimeRange(startDate, endDate), "out of tour date range");
    }

    public TourBind(Long id,
                    @NotNull TourParticipant tourParticipant,
                    @NotNull Tour tour,
                    @NotNull IRange dateTimeRange) throws BindException {
        this(tourParticipant, tour, dateTimeRange);
        this.id = id;

        if (isOutOfTourDateRange())
            throw new BindException(tourParticipant, tour, new LocalDateTimeRange(startDate, endDate), "out of tour date range");
    }

    public TourBind(@NotNull TourParticipant tourParticipant,
                    @NotNull Tour tour,
                    @NotNull LocalDateTime startDate,
                    @NotNull LocalDateTime endDate) throws BindException {
        this(tourParticipant, tour, new LocalDateTimeRange(startDate, endDate));
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
        return tour.equals(tourBind.tour) && tourParticipant.equals(tourBind.tourParticipant) && startDate.equals(tourBind.startDate) && endDate.equals(tourBind.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tour, tourParticipant, startDate, endDate);
    }

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(UI_DATE_TIME_NO_SEC, Locale.ENGLISH);
        return super.toString().replace("}", "") +
                ", tour=" + tour +
                ", tourParticipant=" + tourParticipant +
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
