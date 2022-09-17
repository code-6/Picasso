package org.novinomad.picasso.services;

import org.novinomad.picasso.entities.domain.impl.*;
import org.novinomad.picasso.exceptions.base.BaseException;

import java.util.ArrayList;
import java.util.List;

public interface IDevEnvInitializer {

    List<Tour> createTours();

    List<Driver> createDrivers();

    List<Guide> createGuides();

    List<TourBind> createTourBindings(List<Tour> tours, List<Employee> employees) throws BaseException;

    default List<TourBind> createTourBindings() throws BaseException {
        return createTourBindings(createTours(), createEmployees());
    }

    default List<Employee> createEmployees(){
        List<Employee> employees = new ArrayList<>();

        employees.addAll(createDrivers());
        employees.addAll(createGuides());

        return employees;
    }
}
