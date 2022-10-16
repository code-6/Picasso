package org.novinomad.picasso.entities.domain.impl;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.commons.utils.CommonCollections;
import org.novinomad.picasso.dto.DriverModel;
import org.novinomad.picasso.dto.base.AbstractModel;
import org.novinomad.picasso.entities.base.ModelConvertable;
import org.novinomad.picasso.entities.domain.IDriver;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Driver extends TourParticipant implements IDriver {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(indexes = @Index(columnList = "brandName, modelName"))
    Set<Car> cars = new HashSet<>();

    public Driver(String name) {
        super(name, Type.DRIVER);
    }

    public Driver(Long id, String name) {
        this(name);
        this.id = id;
    }

    @Override
    public DriverModel toModel() {
        DriverModel driverModel = new DriverModel();
        driverModel.setId(id);
        driverModel.setName(name);
        driverModel.setType(type);
        driverModel.setCars(new ArrayList<>(cars));
        return driverModel;
    }

    //region equals, hashCode, toString

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String toStringFull() {
        return super.toString().replace("}","") +
                ", cars=" + cars +
                '}';
    }

    public String getCarsAsString() {
        return CommonCollections.toString(",", cars);
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
            return String.format("%s %s %s", brandName, modelName, number);
        }
    }
    //endregion
}
