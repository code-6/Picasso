package org.novinomad.picasso.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.entities.base.EntityConvertable;
import org.novinomad.picasso.entities.domain.IDriver;
import org.novinomad.picasso.entities.domain.impl.Driver;
import org.novinomad.picasso.entities.domain.impl.TourParticipant;
import org.novinomad.picasso.dto.base.AbstractModel;

import java.util.ArrayList;
import java.util.Collection;
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
