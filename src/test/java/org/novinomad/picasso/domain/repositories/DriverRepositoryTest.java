package org.novinomad.picasso.domain.repositories;

import org.junit.jupiter.api.*;
import org.novinomad.picasso.domain.entities.impl.Driver;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.repositories.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DriverRepositoryTest {

    @Autowired
    DriverRepository driverRepository;

    Employee savedEmployee;

    @Test
    @Order(1)
    void shouldInsertNewDriverWithoutCars() {
        final String name = "Driver";
        Driver driver = new Driver(name);

        assertDoesNotThrow(() -> {
            savedEmployee = driverRepository.save(driver);

            System.out.println(savedEmployee);

            assertNotNull(savedEmployee.getId());
            assertNotNull(savedEmployee.getName());
            assertEquals(name, savedEmployee.getName());
        });
    }

    @Test
    @Order(2)
    void shouldReturnAllDrivers() {
        assertDoesNotThrow(() -> {
            List<Driver> drivers = driverRepository.findAll();

            assertFalse(drivers.isEmpty());
        });
    }

}