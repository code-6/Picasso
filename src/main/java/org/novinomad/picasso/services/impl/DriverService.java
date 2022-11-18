package org.novinomad.picasso.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.erm.entities.Driver;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.repositories.jpa.DriverRepository;
import org.novinomad.picasso.services.IDriverService;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverService implements IDriverService {

    final DriverRepository driverRepository;

    final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Driver save(Driver driver) throws CommonRuntimeException {
        try {
            return driverRepository.save(driver);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to create {} because {}", driver, e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            driverRepository.deleteById(id);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to delete driver with id {} because {}", id, e.getMessage());
        }
    }

    @Override
    public void deleteById(Iterable<Long> ids) throws CommonRuntimeException {
        try {
            driverRepository.deleteAllById(ids);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to delete drivers with ids {} because {}", ids, e.getMessage());
        }
    }

    /**
     * @implNote If drivers not provided wil be removed all items. So be careful when using this method
     * */
    @Override
    @Transactional
    public void delete(Driver... drivers) throws CommonRuntimeException {
        try {
            if(drivers == null || drivers.length == 0) {
                driverRepository.deleteAll();
            } else {
                driverRepository.deleteAll(Arrays.asList(drivers));
            }
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to delete drivers {} because {}", drivers, e.getMessage());
        }
    }

    /**
     * @implNote If ids not provided wil be returned all items.
     * */
    @Override
    public List<Driver> get(Long... ids) throws CommonRuntimeException {
        try {
            if(ids == null || ids.length == 0) {
                return driverRepository.findAll();
            } else {
                return driverRepository.findAllById(Arrays.asList(ids));
            }
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to get drivers by ids {} because {}", ids, e.getMessage());
        }
    }

    @Override
    public Optional<Driver> get(Long id) throws CommonRuntimeException {
        try {
            return driverRepository.findById(id);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to get driver by id {} because {}", id, e.getMessage());
        }
    }

    @Override
    public List<Driver.Car> getAllCars() throws CommonRuntimeException {
        String selectCars = """
                select dc.BRAND_NAME as brandName, dc.MODEL_NAME as modelName 
                from driver_cars dc group by dc.BRAND_NAME, dc.MODEL_NAME
                """;
        try {
            return namedParameterJdbcTemplate.query(selectCars, new MapSqlParameterSource(), (resultSet, rowNum) -> {
                Driver.Car car = new Driver.Car();
                car.setBrandName(resultSet.getString("brandName"));
                car.setModelName(resultSet.getString("modelName"));
                return car;
            });
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to get list of cars because {}", e.getMessage());
        }
    }

    @Override
    public List<String> getCarBrandModels(String carBrand) throws CommonRuntimeException {
        String selectCarBrandModels = """
                select dc.MODEL_NAME as modelName
                from driver_cars dc 
                where dc.BRAND_NAME = :carBrand
                group by dc.MODEL_NAME
                """;
        try {
            return namedParameterJdbcTemplate.query(selectCarBrandModels, new MapSqlParameterSource("carBrand", carBrand),
                    (resultSet, rowNum) -> resultSet.getString("modelName"));
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to get list of car models for brand {} because {}", carBrand, e.getMessage());
        }
    }

    @Override
    public List<String> getCarBrands() throws CommonRuntimeException {
        String selectCarBrandModels = """
                select dc.BRAND_NAME as brandName
                from driver_cars dc group by dc.BRAND_NAME
                """;
        try {
            return namedParameterJdbcTemplate.query(selectCarBrandModels, new MapSqlParameterSource(),
                    (resultSet, rowNum) -> resultSet.getString("brandName"));
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to get list of car brands because {}", e.getMessage());
        }
    }
}
