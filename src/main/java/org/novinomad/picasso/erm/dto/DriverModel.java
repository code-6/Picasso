package org.novinomad.picasso.erm.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.erm.IDriver;
import org.novinomad.picasso.erm.dto.base.AbstractModel;
import org.novinomad.picasso.erm.entities.Driver;
import org.novinomad.picasso.erm.entities.TourParticipant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverModel extends AbstractModel implements IDriver {

    String name;

    TourParticipant.Type type;

    List<Driver.Car> cars = new ArrayList<>();

    @Override
    public List<Driver.Car> getCars() {
        return cars;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Driver toEntity() {
        Driver driver = new Driver();

        driver.setId(id);
        driver.setType(type);
        driver.setName(name);
        driver.setCars(new HashSet<>(cars));

        return driver;
    }
}
