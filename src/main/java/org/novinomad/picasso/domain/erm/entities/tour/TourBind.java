package org.novinomad.picasso.domain.erm.entities.tour;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.commons.exceptions.BindException;
import org.novinomad.picasso.domain.erm.entities.AbstractAuditableEntity;
import org.novinomad.picasso.domain.erm.entities.AbstractEntity;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

import static org.novinomad.picasso.commons.utils.CommonDateUtils.UI_DATE_TIME_NO_SEC;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(indexes = {@Index(columnList = "tour_id,tour_participant_id,start_date,end_date", unique = true),
        @Index(columnList = "CREATE_DATE,CREATED_BY,LAST_UPDATE_DATE,LAST_UPDATED_BY", unique = true)
})
public class TourBind extends AbstractAuditableEntity implements Comparable<TourBind> {

    @OneToOne()
    TourParticipant tourParticipant;

    @OneToOne()
    Tour tour;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDate", column = @Column(name = "start_date", nullable = false)),
            @AttributeOverride(name = "endDate", column = @Column(name = "end_date", nullable = false))
    })
    @NotNull(message = "tour should have date time range")
    LocalDateTimeRange dateTimeRange;

    public TourBind(@NotNull TourParticipant tourParticipant,
                    @NotNull Tour tour,
                    @NotNull LocalDateTimeRange dateTimeRange) throws BindException {
        this.tourParticipant = tourParticipant;
        this.tour = tour;
        this.dateTimeRange = dateTimeRange;

        if (isOutOfTourDateRange())
            throw new BindException(tourParticipant, tour, dateTimeRange, "out of tour date range");
    }

    public TourBind(Long id,
                    @NotNull TourParticipant tourParticipant,
                    @NotNull Tour tour,
                    @NotNull LocalDateTimeRange dateTimeRange) throws BindException {
        this(tourParticipant, tour, dateTimeRange);
        this.id = id;

        if (isOutOfTourDateRange())
            throw new BindException(tourParticipant, tour, dateTimeRange, "out of tour date range");
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

        return getStartDate().isAfter(tourEndDate) || getEndDate().isBefore(tourStartDate);
    }

    //region equals, hashCode, toString

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TourBind tourBind = (TourBind) o;
        return tour.equals(tourBind.tour)
                && tourParticipant.equals(tourBind.tourParticipant)
                && dateTimeRange.equals(tourBind.getDateTimeRange());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tour, tourParticipant, getStartDate(), getEndDate());
    }

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(UI_DATE_TIME_NO_SEC, Locale.ENGLISH);
        return super.toString().replace("}", "") +
                ", tour=" + tour +
                ", tourParticipant=" + tourParticipant +
                ", startDate=" + getStartDate().format(dateTimeFormatter) +
                ", endDate=" + getEndDate().format(dateTimeFormatter) +
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
    public int compareTo(TourBind o) {
        return Comparator.comparing(TourBind::getStartDate)
                .thenComparing(TourBind::getEndDate)
                .compare(this, o);
    }

    //endregion
}
