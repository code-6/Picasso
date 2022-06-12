package org.novinomad.picasso.domain.entities.impl;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Nationalized;
import org.novinomad.picasso.commons.IRange;
import org.novinomad.picasso.domain.entities.ITour;
import org.novinomad.picasso.domain.entities.base.AbstractEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Set;

import static org.novinomad.picasso.commons.utils.CommonDateUtils.ISO_8601;

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
public class Tour extends AbstractEntity implements ITour, IRange {

    @Nationalized
    String name;

    @Nationalized
    @Column(columnDefinition = "NVARCHAR2(1000)")
    String description;

    @DateTimeFormat(pattern = ISO_8601)
    LocalDateTime startDate;

    @DateTimeFormat(pattern = ISO_8601)
    LocalDateTime endDate;

    @ElementCollection
    Set<String> files;

    public Tour(String name, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Tour(Long id, String name, LocalDateTime startDate, LocalDateTime endDate) {
        this(name, startDate, endDate);
        this.id = id;
    }

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
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ISO_8601);
        return super.toString().replace("}", "") +
                ", name='" + name + '\'' +
                ", startDate=" + startDate.format(dateTimeFormatter) +
                ", endDate=" + endDate.format(dateTimeFormatter) +
                '}';
    }

    @Override
    public String toStringFull() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ISO_8601);
        return super.toString().replace("}", "") +
                ", name='" + name + '\'' +
                ", startDate=" + startDate.format(dateTimeFormatter) +
                ", endDate=" + endDate.format(dateTimeFormatter) +
                ", files=" + files +
                '}';
    }
    //endregion

}
