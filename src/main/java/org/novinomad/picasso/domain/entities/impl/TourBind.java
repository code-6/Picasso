package org.novinomad.picasso.domain.entities.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.domain.entities.base.AbstractEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"tour_id", "employee_id", "startDate", "endDate"}))
public class TourBind extends AbstractEntity {

    @OneToOne
    Tour tour;

    @OneToOne
    Employee employee;

    @Column(nullable = false)
    LocalDateTime startDate;

    @Column(nullable = false)
    LocalDateTime endDate;

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

    //endregion
}
