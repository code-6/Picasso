package org.novinomad.picasso.entities.domain.impl;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Nationalized;
import org.novinomad.picasso.commons.IRange;
import org.novinomad.picasso.entities.domain.ITour;
import org.novinomad.picasso.entities.base.AbstractEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.novinomad.picasso.commons.utils.CommonDateUtils.UI_DATE_TIME_NO_SEC;

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

    
    LocalDateTime startDate;

    
    LocalDateTime endDate;

    @ElementCollection
    Set<String> fileNames = new HashSet<>();

    @Override
    public Set<String> getFileNames() {
        return fileNames;
    }

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
        try {

            return (int) ChronoUnit.DAYS.between(startDate, endDate);
        } catch (NullPointerException e) {
            return 0;
        }
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
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(UI_DATE_TIME_NO_SEC, Locale.ENGLISH);
        return super.toString().replace("}", "") +
                ", name='" + name + '\'' +
                ", startDate=" + (startDate == null ? null : startDate.format(dateTimeFormatter)) +
                ", endDate=" + (endDate == null ? null : endDate.format(dateTimeFormatter)) +
                '}';
    }

    @Override
    public String toStringFull() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(UI_DATE_TIME_NO_SEC, Locale.ENGLISH);
        return super.toString().replace("}", "") +
                ", name='" + name + '\'' +
                ", startDate=" + (startDate == null ? null : startDate.format(dateTimeFormatter)) +
                ", endDate=" + (endDate == null ? null : endDate.format(dateTimeFormatter)) +
                ", files=" + fileNames +
                '}';
    }
    //endregion

}
