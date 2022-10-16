package org.novinomad.picasso.entities.domain.impl;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Nationalized;
import org.novinomad.picasso.dto.base.AbstractModel;
import org.novinomad.picasso.dto.gantt.Task;
import org.novinomad.picasso.entities.base.AbstractEntity;
import org.novinomad.picasso.entities.base.ModelConvertable;

import javax.persistence.*;
import java.util.Objects;


@FieldDefaults(level = AccessLevel.PROTECTED)
@Getter
@Setter
@Table(indexes = @Index(columnList = "name"))
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Guide.class, name = "GUIDE"),
        @JsonSubTypes.Type(value = Driver.class, name = "DRIVER")
})
public class TourParticipant extends AbstractEntity {

    @Nationalized
    @Column(nullable = false)
    String name;

    @Enumerated(EnumType.STRING)
    Type type;

    @Override
    public String toString() {
        return super.toString().replace("}","") +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TourParticipant tourParticipant = (TourParticipant) o;
        return Objects.equals(name, tourParticipant.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    @Override
    public String toStringFull() {
        return super.toString().replace("}","") +
                ", type='" + type.name() + '\'' +
                ", name='" + name + '\'' +
                '}';
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
