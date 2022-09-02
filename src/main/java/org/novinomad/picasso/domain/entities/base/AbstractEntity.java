package org.novinomad.picasso.domain.entities.base;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.commons.PrettyPrintable;
import org.novinomad.picasso.domain.IdAware;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass
@FieldDefaults(level = AccessLevel.PROTECTED)
@Getter
@Setter
public abstract class AbstractEntity implements PrettyPrintable, IdAware<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +"{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity that = (AbstractEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
