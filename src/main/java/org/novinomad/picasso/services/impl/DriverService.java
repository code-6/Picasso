package org.novinomad.picasso.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.domain.entities.impl.Driver;
import org.novinomad.picasso.exceptions.base.PicassoException;
import org.novinomad.picasso.repositories.jpa.DriverRepository;
import org.novinomad.picasso.services.IDriverService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverService implements IDriverService {

    final DriverRepository driverRepository;


    @Override
    public Driver save(Driver driver) throws PicassoException {
        try {
            Driver savedDriver = driverRepository.save(driver);
            log.debug("saved {}", savedDriver);
            return savedDriver;
        } catch (Exception e) {
            log.error("unable to create: {} because: {}", driver, e.getMessage(), e);
            throw new PicassoException(e, "unable to create: {} because: {}", driver, e.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws PicassoException {
        try {
            driverRepository.deleteById(id);
        } catch (Exception e) {
            log.error("unable to delete Driver with id: {} because: {}", id, e.getMessage(), e);
            throw new PicassoException(e, "unable to delete Driver with id: {} because: {}", id, e.getMessage());
        }
    }
}
