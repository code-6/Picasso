package org.novinomad.picasso.services;

import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.commons.exceptions.base.CommonException;
import org.novinomad.picasso.erm.entities.Driver;

import java.util.List;

public interface IDriverService extends ICrud<Long, Driver> {
    List<Driver.Car> getAllCars();

    List<String> getCarBrandModels(String carBrand);

    List<String> getCarBrands();
}
