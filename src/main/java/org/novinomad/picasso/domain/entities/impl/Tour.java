package org.novinomad.picasso.domain.entities.impl;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.util.StringUtil;
import org.hibernate.annotations.Nationalized;
import org.novinomad.picasso.commons.IRange;
import org.novinomad.picasso.commons.utils.CommonDateUtils;
import org.novinomad.picasso.domain.entities.ITour;
import org.novinomad.picasso.domain.entities.base.AbstractEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import static org.novinomad.picasso.commons.utils.CommonDateUtils.COMMON;
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

    @DateTimeFormat(pattern = COMMON)
    LocalDateTime startDate;

    @DateTimeFormat(pattern = COMMON)
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
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(COMMON, Locale.ENGLISH);
        return super.toString().replace("}", "") +
                ", name='" + name + '\'' +
                ", startDate=" + (startDate == null ? null : startDate.format(dateTimeFormatter)) +
                ", endDate=" + (endDate == null ? null : endDate.format(dateTimeFormatter)) +
                '}';
    }

    @Override
    public String toStringFull() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(COMMON, Locale.ENGLISH);
        return super.toString().replace("}", "") +
                ", name='" + name + '\'' +
                ", startDate=" + (startDate == null ? null : startDate.format(dateTimeFormatter)) +
                ", endDate=" + (endDate == null ? null : endDate.format(dateTimeFormatter)) +
                ", files=" + files +
                '}';
    }

    public String getEndDate(String format) {
        if(endDate == null) return "";

        if(StringUtils.isEmpty(format))
            format = COMMON;

        return endDate.format(DateTimeFormatter.ofPattern(format, Locale.ENGLISH));
    }

    public String getStartDate(String format) {
        if(startDate == null) return "";

        if(StringUtils.isEmpty(format))
            format = COMMON;

        return startDate.format(DateTimeFormatter.ofPattern(format, Locale.ENGLISH));
    }
    //endregion

}
