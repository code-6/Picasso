package org.novinomad.picasso.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.entities.domain.impl.Driver;
import org.novinomad.picasso.exceptions.base.BaseException;
import org.novinomad.picasso.repositories.jpa.DriverRepository;
import org.novinomad.picasso.services.IDriverService;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverService implements IDriverService {

    final DriverRepository driverRepository;

    final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Driver save(Driver driver) throws BaseException {
        try {
            Driver savedDriver = driverRepository.save(driver);
            log.debug("saved {}", savedDriver);
            return savedDriver;
        } catch (Exception e) {
            log.error("unable to create: {} because: {}", driver, e.getMessage(), e);
            throw new BaseException(e, "unable to create: {} because: {}", driver, e.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws BaseException {
        try {
            driverRepository.deleteById(id);
        } catch (Exception e) {
            log.error("unable to delete Driver with id: {} because: {}", id, e.getMessage(), e);
            throw new BaseException(e, "unable to delete Driver with id: {} because: {}", id, e.getMessage());
        }
    }

    @Override
    public List<Driver.Car> getAllCars() {
        String selectCars = """
                select dc.BRAND_NAME as brandName, dc.MODEL_NAME as modelName 
                from driver_cars dc group by dc.BRAND_NAME, dc.MODEL_NAME
                """;
        return namedParameterJdbcTemplate.query(selectCars, new MapSqlParameterSource(), (resultSet, rowNum) -> {
            Driver.Car car = new Driver.Car();
            car.setBrandName(resultSet.getString("brandName"));
            car.setModelName(resultSet.getString("modelName"));
            return car;
        });
    }

    @Override
    public List<String> getCarBrandModels(String carBrand) {
        String selectCarBrandModels = """
                select dc.MODEL_NAME as modelName
                from driver_cars dc 
                where dc.BRAND_NAME = :carBrand
                group by dc.MODEL_NAME
                """;
        return namedParameterJdbcTemplate.query(selectCarBrandModels, new MapSqlParameterSource("carBrand", carBrand),
                (resultSet, rowNum) -> resultSet.getString("modelName"));
    }

    @Override
    public List<String> getCarBrands() {
        String selectCarBrandModels = """
                select dc.BRAND_NAME as brandName
                from driver_cars dc group by dc.BRAND_NAME
                """;
        return namedParameterJdbcTemplate.query(selectCarBrandModels, new MapSqlParameterSource(),
                (resultSet, rowNum) -> resultSet.getString("brandName"));
    }
}
