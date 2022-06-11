package org.novinomad.picasso.domain.entities.impl;

import com.sun.istack.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.commons.utils.CommonCollectionUtils;
import org.novinomad.picasso.domain.entities.IDriver;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Driver extends Employee implements IDriver {

    @ElementCollection(fetch = FetchType.EAGER)
    Set<Car> cars = new HashSet<>();

    public Driver(String name) {
        super(name);
    }

    //region equals, hashCode, toString
    @Override
    public String toString() {
        return super.toString() + String.format("\tcars: %s\n", CommonCollectionUtils.toString("|", cars));
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
    @Table(indexes = @Index(columnList = "number"))
    public static class Car {
        String brandName;
        String modelName;
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
            return brandName + "," + modelName + "," + number;
        }

        public static Car fromString(@NotNull String carString) throws IllegalArgumentException {
            String[] arr = carString.split(",");
            try {
                return new Car(String.valueOf(arr[0]),  String.valueOf(arr[1]),  String.valueOf(arr[2]));
            } catch (ArrayIndexOutOfBoundsException e) {
                String exceptionMessage = String.format("Unable to build Car from following string: %s", carString);
                throw new IllegalArgumentException(exceptionMessage);
            }
        }
    }

    public static class CarsToStringConverter implements AttributeConverter<Collection<Car>, String> {

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
    }
    //endregion
}
