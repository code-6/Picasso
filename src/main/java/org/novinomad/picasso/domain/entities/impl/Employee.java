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
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee extends AbstractEntity {

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
        Employee employee = (Employee) o;
        return name.equals(employee.name);
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

    public enum Type {
        DRIVER,
        GUIDE;
    }
}
