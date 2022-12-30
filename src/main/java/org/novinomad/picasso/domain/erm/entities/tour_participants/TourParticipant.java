package org.novinomad.picasso.domain.erm.entities.tour_participants;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.novinomad.picasso.domain.dto.tour.gantt.Task;
import org.novinomad.picasso.domain.erm.entities.AbstractAuditableEntity;
import org.novinomad.picasso.domain.erm.entities.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;


@FieldDefaults(level = AccessLevel.PROTECTED)
@Getter
@Setter
@Table(indexes = @Index(columnList = "name, type"))
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Guide.class, name = "GUIDE"),
        @JsonSubTypes.Type(value = Driver.class, name = "DRIVER")
})
public class TourParticipant extends AbstractAuditableEntity {

    @Nationalized
    @Column(nullable = false)
    @NotBlank(message = "name may bot be blank")
    String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "tour participant type can't be null")
    Type type;

    @Override
    public String toString() {
        return super.toString().replace("}","") +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TourParticipant that = (TourParticipant) o;
        return Objects.equals(name, that.name) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, type);
    }

    @RequiredArgsConstructor
    public enum Type {
        DRIVER("fas fa-user-tie", Task.CssClass.PINK, Driver.class, "driver"),
        GUIDE("fas fa-hiking", Task.CssClass.YELLOW, Guide.class, "guide");

        private final String ICON_CSS_CLASS;

        private final Task.CssClass GANT_TASK_COLOR;
        private final Class<? extends TourParticipant> TOUR_PARTICIPANT_CLASS;

        private final String THYMELEAF_FRAGMENT;

        public Class<? extends TourParticipant> getTourParticipantClass() {
            return TOUR_PARTICIPANT_CLASS;
        }

        public String getIconCssClass() {
            return this.ICON_CSS_CLASS;
        }

        public Task.CssClass getGanttTaskColor() {
            return this.GANT_TASK_COLOR;
        }

        public String getThymeleafFragment() {
            return THYMELEAF_FRAGMENT;
        }
    }
}
