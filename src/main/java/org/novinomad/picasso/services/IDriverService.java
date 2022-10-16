package org.novinomad.picasso.services;

import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.entities.domain.impl.Driver;

import java.util.List;

public interface IDriverService extends ICrud<Driver> {
    List<Driver.Car> getAllCars();

    List<String> getCarBrandModels(String carBrand);

    List<String> getCarBrands();
}
