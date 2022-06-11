package org.novinomad.picasso.domain.entities.impl;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Nationalized;
import org.novinomad.picasso.domain.entities.base.AbstractEntity;

import javax.persistence.*;
import java.util.Objects;


@FieldDefaults(level = AccessLevel.PROTECTED)
@Getter
@Setter
@Table(indexes = @Index(columnList = "name"))
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee extends AbstractEntity {

    @Nationalized
    @Column(nullable = false)
    String name;

    @Override
    public String toString() {
        return super.toString() + String.format("\tname: %s\n", name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Employee employee = (Employee) o;
        return name.equals(employee.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    //region NESTED CLASSES
    public enum Type {
        DRIVER,
        GUIDE
    }
    //endregion
}
