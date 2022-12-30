package org.novinomad.picasso.domain.dto.tour_participants;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.domain.dto.AbstractDto;
import org.novinomad.picasso.domain.IDriver;
import org.novinomad.picasso.domain.erm.entities.tour_participants.Driver;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverDto extends AbstractDto implements IDriver {

    String name;

    TourParticipant.Type type;

    @Builder.Default
    List<Driver.Car> cars = new LinkedList<>();

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
        Driver driver = new Driver(name);

        driver.setId(id);
        driver.setType(type);
        driver.setCars(cars);

        return driver;
    }
}
