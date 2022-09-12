package org.novinomad.picasso.repositories;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.novinomad.picasso.entities.domain.impl.Driver;
import org.novinomad.picasso.entities.domain.impl.Employee;
import org.novinomad.picasso.repositories.jpa.EmployeeRepository;
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
class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    Faker faker;

    Employee savedEmployee;

    @Test
    @Order(1)
    void shouldInsertNewDriverWithoutCars() {
        final String name = faker.name().fullName();

        Driver driver = new Driver(name);

        assertDoesNotThrow(() -> {
            savedEmployee = employeeRepository.save(driver);

            System.out.println(savedEmployee);

            assertNotNull(savedEmployee.getId());
            assertNotNull(savedEmployee.getName());
            assertEquals(name, savedEmployee.getName());
        });
    }

    @Test
    @Order(2)
    void shouldInsertNewDriverWithCars() {
        final String name = faker.name().fullName();

        Driver driver = new Driver(name);
        driver.addCar("Nissan", "GTR R33", String.valueOf(faker.number().numberBetween(1000, 9999)));
        driver.addCar("Toyota", "Supra", String.valueOf(faker.number().numberBetween(1000, 9999)));
        driver.addCar("Mazda", "RX-7", String.valueOf(faker.number().numberBetween(1000, 9999)));

        assertDoesNotThrow(() -> {
            savedEmployee = employeeRepository.save(driver);

            System.out.println(savedEmployee);

            assertNotNull(savedEmployee.getId());
            assertNotNull(savedEmployee.getName());
            assertEquals(name, savedEmployee.getName());
        });
    }

    @Test
    @Order(3)
    void shouldReturnAllDrivers() {
        assertDoesNotThrow(() -> {
            List<Employee> drivers = employeeRepository.findAll();

            assertFalse(drivers.isEmpty());
        });
    }

}