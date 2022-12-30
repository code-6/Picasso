package org.novinomad.picasso.services.tour_participants;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.commons.enums.CRUD;
import org.novinomad.picasso.commons.exceptions.CRUDException;
import org.novinomad.picasso.domain.erm.entities.tour_participants.Driver;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.repositories.jpa.DriverRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverServiceImpl implements DriverService {

    final DriverRepository driverRepository;

    final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Driver save(@NotNull Driver driver) {
        return driverRepository.save(driver);
    }

    @Override
    public void deleteById(@NotNull Long id) {
        driverRepository.deleteById(id);
    }

    @Override
    public void deleteById(@NotEmpty Collection<Long> ids) throws CommonRuntimeException {
        driverRepository.deleteAllById(ids);
    }

    @Override
    public List<Driver> getById(@NotEmpty Collection<Long> ids) throws CommonRuntimeException {
        return driverRepository.findAllById(ids);
    }

    @Override
    public List<Driver> get() {
        return driverRepository.findAll();
    }

    @Override
    public Optional<Driver> getById(@NotNull Long id) throws CommonRuntimeException {
        return driverRepository.findById(id);
    }

    // todo: move to separate service

    @Override
    public List<Driver.Car> getAllCars() throws CommonRuntimeException {
        String selectCars = """
                select distinct dc.BRAND_NAME as brandName, dc.MODEL_NAME as modelName from driver_cars dc 
                """;
        return namedParameterJdbcTemplate.query(selectCars, new MapSqlParameterSource(), (resultSet, rowNum) -> {
            Driver.Car car = new Driver.Car();
            car.setBrandName(resultSet.getString("brandName"));
            car.setModelName(resultSet.getString("modelName"));
            return car;
        });
    }

    @Override
    public List<String> getCarBrandModels(String carBrand) throws CommonRuntimeException {
        String selectCarBrandModels = """
                select distinct dc.MODEL_NAME as modelName from driver_cars dc where dc.BRAND_NAME = :carBrand
                """;
        return namedParameterJdbcTemplate.query(selectCarBrandModels, new MapSqlParameterSource("carBrand", carBrand),
                (resultSet, rowNum) -> resultSet.getString("modelName"));
    }

    @Override
    public List<String> getCarBrands() throws CommonRuntimeException {
        String selectCarBrandModels = """
                select distinct dc.BRAND_NAME as brandName from driver_cars dc
                """;
        return namedParameterJdbcTemplate.query(selectCarBrandModels, new MapSqlParameterSource(),
                (resultSet, rowNum) -> resultSet.getString("brandName"));
    }
}
