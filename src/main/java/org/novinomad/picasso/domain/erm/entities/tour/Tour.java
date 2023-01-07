package org.novinomad.picasso.domain.erm.entities.tour;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Nationalized;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.domain.ITour;
import org.novinomad.picasso.domain.erm.entities.AbstractAuditableEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(indexes = {
//        @Index(columnList = "start_date, end_date", name = "tour_date_range_idx"),
//        @Index(columnList = "name", name = "tour_name_idx"),
        @Index(columnList = "name, start_date, end_date", name = "tour_date_name_idx", unique = true)
})
public class Tour extends AbstractAuditableEntity implements ITour, Comparable<Tour> {

    @Nationalized
    @Column(nullable = false)
    @NotBlank(message = "tour name may not be blank")
    String name;

    @Nationalized
    @Column(length = 1000)
    String description;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDate", column = @Column(name = "start_date", nullable = false)),
            @AttributeOverride(name = "endDate", column = @Column(name = "end_date", nullable = false))
    })
    @NotNull(message = "tour should have date time range")
    LocalDateTimeRange dateTimeRange;

    @Setter(AccessLevel.PROTECTED)
    @ElementCollection(fetch = FetchType.EAGER)
    List<String> fileNames = new ArrayList<>();

    @Override
    public List<String> getFileNames() {
        return fileNames;
    }

    public Tour() {
        dateTimeRange = new LocalDateTimeRange();
    }

    public Tour(String name, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        dateTimeRange = new LocalDateTimeRange(startDate, endDate);
    }

    public Tour(Long id, String name, LocalDateTime startDate, LocalDateTime endDate) {
        this(name, startDate, endDate);
        this.id = id;
    }

    public int getDaysCount() {
        return (int) dateTimeRange.getDuration().toDays();
    }

    //region equals, hashCode, toString


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Tour tour = (Tour) o;
        return name.equals(tour.name) && Objects.equals(description, tour.description) && dateTimeRange.equals(tour.dateTimeRange) && fileNames.equals(tour.fileNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, dateTimeRange, fileNames);
    }

    @Override
    public String toString() {
        return super.toString().replace("}", "") +
                ", name='" + name + '\'' +
                ", dateTimeRange=" + (dateTimeRange == null ? null : dateTimeRange.toString()) +
                ", files=" + fileNames +
                '}';
    }

    public LocalDateTime getStartDate() {
        return dateTimeRange.getStartDate();
    }

    public LocalDateTime getEndDate() {
        return dateTimeRange.getEndDate();
    }

    public void setStartDate(LocalDateTime startDate) {
        dateTimeRange.setStartDate(startDate);
    }

    public void setEndDate(LocalDateTime endDate) {
        dateTimeRange.setEndDate(endDate);
    }

    @Override
    public int compareTo(Tour o) {
        return Comparator.comparing(Tour::getStartDate)
                .thenComparing(Tour::getEndDate)
                .compare(this, o);
    }
    //endregion

}
