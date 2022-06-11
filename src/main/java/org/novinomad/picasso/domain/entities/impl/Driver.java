package org.novinomad.picasso.domain.entities.impl;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.domain.entities.IDriver;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Driver extends Employee implements IDriver {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(indexes = @Index(columnList = "brandName, modelName"))
    Set<Car> cars = new HashSet<>();

    public Driver(String name) {
        super(name);
    }

    //region equals, hashCode, toString


    @Override
    public String toString() {
        return super.toString().replace("}","") +
                ", cars=" + cars +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Driver driver = (Driver) o;
        return Objects.equals(cars, driver.cars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cars);
    }
    //endregion

    //region NESTED CLASSES
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class Car {

        private static final Class<Car> clazz = Car.class;

        String brandName;
        String modelName;

        @Column(unique = true)
        String number;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Car car = (Car) o;
            return Objects.equals(brandName, car.brandName) && Objects.equals(modelName, car.modelName) && Objects.equals(number, car.number);
        }

        @Override
        public int hashCode() {
            return Objects.hash(brandName, modelName, number);
        }

        @Override
        public String toString() {
            return "Car{" +
                    "brandName='" + brandName + '\'' +
                    ", modelName='" + modelName + '\'' +
                    ", number='" + number + '\'' +
                    '}';
        }
    }

    /*public static class CarsToStringConverter implements AttributeConverter<Collection<Car>, String> {

        private static final String delimiter = ",";

        @Override
        public String convertToDatabaseColumn(Collection<Car> attribute) {
            return attribute.stream()
                    .map(Car::toString)
                    .sorted()
                    .collect(Collectors.joining(delimiter));
        }

        @Override
        public Collection<Car> convertToEntityAttribute(String dbData) {
            return Arrays.stream(dbData.split(dbData))
                    .map(Car::fromString)
                    .collect(Collectors.toSet());
        }
    }*/
    //endregion
}
