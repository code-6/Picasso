package org.novinomad.picasso.entities.domain;

import org.novinomad.picasso.entities.domain.impl.Driver;

import java.util.*;

public interface IDriver {
    List<Driver.Car> getCars();

    default void addCar(Driver.Car ... cars) {
        getCars().addAll(Arrays.asList(cars));
    }

    default void addCar(Driver.Car car) {
        getCars().add(car);
    }

    default void addCar(Collection<Driver.Car> cars) {
        getCars().addAll(cars);
    }

    default void addCar(String brandName, String modelName, String number) {
        addCar(new Driver.Car(brandName, modelName, number));
    }

    default void removeCar(Driver.Car car) {
        getCars().removeIf(lang -> lang.equals(car));
    }

    default void removeCar(Driver.Car ... car) {
        Arrays.asList(car).forEach(getCars()::remove);
    }

    default void removeCar(Collection<Driver.Car> cars) {
        getCars().removeAll(cars);
    }

    default boolean hasCar(Driver.Car car) {
        return getCars().contains(car);
    }

    default boolean hasCar(Driver.Car ... car) {
        return new HashSet<>(getCars()).containsAll(Arrays.asList(car));
    }

    default boolean hasCar(Collection<Driver.Car> cars) {
        return new HashSet<>(getCars()).containsAll(cars);
    }
}
