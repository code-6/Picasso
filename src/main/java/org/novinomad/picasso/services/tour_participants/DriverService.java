package org.novinomad.picasso.services.tour_participants;

import org.novinomad.picasso.commons.Crud;
import org.novinomad.picasso.domain.erm.entities.tour_participants.Driver;

import javax.validation.constraints.NotBlank;
import java.util.List;

public interface DriverService extends Crud<Long, Driver> {
    List<Driver.Car> getAllCars();

    List<String> getCarBrandModels(@NotBlank String carBrand);

    List<String> getCarBrands();
}
