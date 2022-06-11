package org.novinomad.picasso.domain.entities;

import org.novinomad.picasso.domain.entities.impl.Driver;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public interface IDriver {
    Set<Driver.Car> getCars();

    default void addCar(Driver.Car ... cars) {
        getCars().addAll(Arrays.asList(cars));
    }

    default void addCar(Driver.Car car) {
        getCars().add(car);
    }

    default void addCar(Collection<Driver.Car> cars) {
        getCars().addAll(cars);
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
        return getCars().containsAll(Arrays.asList(car));
    }

    default boolean hasCar(Collection<Driver.Car> cars) {
        return getCars().containsAll(cars);
    }
}
