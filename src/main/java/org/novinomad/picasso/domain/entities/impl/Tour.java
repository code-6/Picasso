package org.novinomad.picasso.domain.entities.impl;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Nationalized;
import org.novinomad.picasso.domain.entities.ITour;
import org.novinomad.picasso.domain.entities.base.AbstractEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(indexes = {
        @Index(columnList = "startDate, endDate", name = "tour_date_range_idx"),
        @Index(columnList = "name", name = "tour_name_idx")
})
public class Tour extends AbstractEntity implements ITour {

    @Nationalized
    String name;

    @Nationalized
    @Column(columnDefinition = "NVARCHAR2(1000)")
    String description;

    LocalDateTime startDate;

    LocalDateTime endDate;

    @ElementCollection
    Set<String> files;

    public int getDaysCount() {
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    //region equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Tour tour = (Tour) o;
        return name.equals(tour.name) && Objects.equals(description, tour.description) && Objects.equals(startDate, tour.startDate) && Objects.equals(endDate, tour.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, startDate, endDate);
    }

    @Override
    public String toString() {
        return super.toString().replace("}", "") +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", files=" + files +
                '}';
    }

    //endregion

}
