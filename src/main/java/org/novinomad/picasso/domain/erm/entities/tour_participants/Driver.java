package org.novinomad.picasso.domain.erm.entities.tour_participants;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.novinomad.picasso.commons.collections.CommonCollections;
import org.novinomad.picasso.domain.dto.tour_participants.DriverDto;
import org.novinomad.picasso.domain.IDriver;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Driver extends TourParticipant implements IDriver {

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(indexes = @Index(columnList = "brandName, modelName"))
    List<Car> cars = new LinkedList<>();

    public Driver(String name) {
        super(name, Type.DRIVER);
    }

    public Driver(Long id, String name) {
        this(name);
        this.id = id;
    }

    @Override
    public DriverDto toModel() {
        DriverDto driverModel = new DriverDto();
        driverModel.setId(id);
        driverModel.setName(name);
        driverModel.setType(type);
        driverModel.setCars(new ArrayList<>(cars));
        return driverModel;
    }

    //region equals, hashCode, toString

    @Override
    public String toString() {
        return super.toString().replace("}","") +
                ", cars=" + cars +
                '}';
    }

    public String getCarsAsString() {
        return CommonCollections.toString("; ", "", "",cars);
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
