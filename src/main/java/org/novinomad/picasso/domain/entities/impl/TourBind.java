package org.novinomad.picasso.domain.entities.impl;

import com.sun.istack.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.domain.entities.base.AbstractEntity;
import org.novinomad.picasso.exceptions.TourBindException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(indexes = @Index(columnList = "tour_id,employee_id,startDate,endDate", unique = true))
public class TourBind extends AbstractEntity {

    @OneToOne
    Employee employee;

    @OneToOne
    Tour tour;

    @Column(nullable = false)
    LocalDateTime startDate;

    @Column(nullable = false)
    LocalDateTime endDate;

    public TourBind(@NotNull Employee employee,
                    @NotNull Tour tour,
                    @NotNull LocalDateTime startDate,
                    @NotNull LocalDateTime endDate) throws TourBindException
    {
        this.employee = employee;
        this.tour = tour;
        this.startDate = startDate;
        this.endDate = endDate;

        if(!isDatesValid())
            throw new TourBindException(employee, tour, startDate, endDate, "out of tour date range");
    }

    boolean isDatesValid() {
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
        return super.toString().replace("}", "") +
                ", tour=" + tour +
                ", employee=" + employee +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    //endregion
}
