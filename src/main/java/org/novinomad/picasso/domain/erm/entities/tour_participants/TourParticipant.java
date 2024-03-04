package org.novinomad.picasso.domain.erm.entities.tour_participants;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Nationalized;
import org.novinomad.picasso.domain.dto.tour.gantt.Task;
import org.novinomad.picasso.domain.erm.entities.AbstractAuditableEntity;
import org.novinomad.picasso.domain.erm.entities.tour.TourBind;
import org.springframework.data.util.Pair;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;


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
public class TourParticipant extends AbstractAuditableEntity implements Comparable<TourParticipant> {

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

    @Override
    public int compareTo(TourParticipant o) {
        return Comparator.comparing(TourParticipant::getType)
                .thenComparing(TourParticipant::getName)
                .compare(this, o);
    }

    public enum Type {
        DRIVER("fas fa-user-tie", Task.CssClass.PINK, Driver.class, "driver",
                Pair.of(Locale.US, "Driver"),
                Pair.of(new Locale("RU"), "Водитель")),
        GUIDE("fas fa-hiking", Task.CssClass.YELLOW, Guide.class, "guide",
                Pair.of(Locale.US, "Guide"),
                Pair.of(new Locale("RU"), "Гид"));

        private final String ICON_CSS_CLASS;

        private final Task.CssClass GANT_TASK_COLOR;
        private final Class<? extends TourParticipant> TOUR_PARTICIPANT_CLASS;

        private final String THYMELEAF_FRAGMENT;

        private final Map<Locale, String> INTERNATIONALIZED_CONSTANT_NAMES = new HashMap<>();

        Type(String ICON_CSS_CLASS, Task.CssClass GANT_TASK_COLOR, Class<? extends TourParticipant> TOUR_PARTICIPANT_CLASS, String THYMELEAF_FRAGMENT, Pair<Locale, String> ... internationalizedNames) {
            this.ICON_CSS_CLASS = ICON_CSS_CLASS;
            this.GANT_TASK_COLOR = GANT_TASK_COLOR;
            this.TOUR_PARTICIPANT_CLASS = TOUR_PARTICIPANT_CLASS;
            this.THYMELEAF_FRAGMENT = THYMELEAF_FRAGMENT;
            for (Pair<Locale, String> internationalizedName : internationalizedNames) {
                INTERNATIONALIZED_CONSTANT_NAMES.put(internationalizedName.getFirst(), internationalizedName.getSecond());
            }
        }

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

        public Type addInternationalizedName(Locale locale, String name) {
            INTERNATIONALIZED_CONSTANT_NAMES.put(locale, name);
            return this;
        }

        public String getInternationalizedName(Locale locale) {
            return INTERNATIONALIZED_CONSTANT_NAMES.get(locale);
        }
    }
}
