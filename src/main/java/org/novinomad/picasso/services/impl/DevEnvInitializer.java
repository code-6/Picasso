package org.novinomad.picasso.services.impl;

import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.novinomad.picasso.commons.utils.CarUtils;
import org.novinomad.picasso.commons.utils.CommonDateUtils;
import org.novinomad.picasso.domain.entities.impl.*;
import org.novinomad.picasso.exceptions.BindException;
import org.novinomad.picasso.exceptions.base.PicassoException;
import org.novinomad.picasso.repositories.jpa.DriverRepository;
import org.novinomad.picasso.repositories.jpa.GuideRepository;
import org.novinomad.picasso.repositories.jpa.TourRepository;
import org.novinomad.picasso.services.IDevEnvInitializer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.novinomad.picasso.commons.utils.CommonDateUtils.dateToLocalDateTime;
import static org.novinomad.picasso.commons.utils.CommonDateUtils.localDateTimeToDate;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Profile({"dev", "test"})
public class DevEnvInitializer implements IDevEnvInitializer {
    static final LocalDate CURRENT_DATE = LocalDate.now();
    static final int NEXT_MONTH_MAX_DAY = CURRENT_DATE.plusMonths(1).lengthOfMonth();
    static final int EMPLOYEES_COUNT_TO_CREATE = 10;
    static final int TOURS_COUNT_TO_CREATE = 100;
    static final int BINDINGS_COUNT_TO_CREATE = 60;
    static final int MAX_TOUR_DAYS_COUNT = 20;
    static final int MIN_TOUR_DAYS_COUNT = 10;
    static final Date minDate;
    static final Date maxDate;

    static {
        minDate = CommonDateUtils.localDateTimeToDate(CURRENT_DATE.minusMonths(1).atStartOfDay());
        maxDate = CommonDateUtils.localDateTimeToDate(CURRENT_DATE.plusMonths(2).atStartOfDay());
    }

    final Faker faker;
    final GuideRepository guideRepository;
    final DriverRepository driverRepository;
    final TourRepository tourRepository;
    final TourBindService tourBindService;

    @PostConstruct
    void init() {
        log.info("initialize DB with test data");
        try {
            List<Employee> employees = createEmployees();
            List<Tour> tours = createTours();
//            createTourBindings(tours, employees);
        } catch (Exception e) {
            log.error("unable to initialize DB with test data because: {}", e.getMessage());
        }
    }

    @Override
    public List<Tour> createTours() {
        log.info("start create allTours");
        List<Tour> tours = new ArrayList<>();
        for (int i = 0; i < TOURS_COUNT_TO_CREATE; i++) {
            String name = faker.name().title();
            String desc = faker.yoda().quote();
            Date startDate = faker.date().between(minDate, maxDate);
            int rndTourDuration = RandomUtils.nextInt(MIN_TOUR_DAYS_COUNT, MAX_TOUR_DAYS_COUNT);
            Calendar instance = Calendar.getInstance();
            instance.setTime(startDate);
            instance.add(Calendar.DATE, rndTourDuration);
            Date endDate = faker.date().between(startDate, instance.getTime());

            LocalDateTime finalStartDate = dateToLocalDateTime(startDate);
            LocalDateTime finalEndDate = dateToLocalDateTime(endDate);

            Set<String> files = new HashSet<>();

            int rndFilesCount = RandomUtils.nextInt(0, 3);

            for (int j = 0; j < rndFilesCount; j++) {
                files.add(faker.file().fileName());
            }

            Tour tour = new Tour(name, desc, finalStartDate, finalEndDate, files);

            try {
                Tour savedTour = tourRepository.save(tour);
                log.debug("created new {}", savedTour);
                tours.add(savedTour);
            } catch (Exception e) {
                log.error("Unable to create: {} because: {}", tour, e.getMessage(), e);
            }
        }
        return tours;
    }

    @Override
    public List<Driver> createDrivers() {
        log.info("start create drivers");
        List<Driver> drivers = new ArrayList<>();
        final int maxCarsCount = 3;
        final int minCarsCount = 1;

        for (int i = 0; i < EMPLOYEES_COUNT_TO_CREATE; i++) {
            final String driverName = faker.name().fullName();
            Driver driver = new Driver(driverName);
            driver.addCar(CarUtils.randomSet(minCarsCount, maxCarsCount));
            try {
                Driver savedDriver = driverRepository.save(driver);
                log.debug("created new {}", savedDriver);
                drivers.add(savedDriver);
            } catch (Exception e) {
                log.error("Unable to create: {} because: {}", driver, e.getMessage(), e);
            }
        }
        return drivers;
    }

    @Override
    public List<Guide> createGuides() {
        log.info("start create guides");
        List<Guide> guides = new ArrayList<>();
        final int maxLanguagesCount = 3;
        final int minLanguagesCount = 1;

        for (int i = 0; i < EMPLOYEES_COUNT_TO_CREATE; i++) {
            final String guideName = faker.name().fullName();
            Guide guide = new Guide(guideName);
            guide.addLanguage(Guide.Language.randomSet(minLanguagesCount, maxLanguagesCount));
            try {
                Guide savedGuide = guideRepository.save(guide);
                log.debug("created new {}", savedGuide);
                guides.add(savedGuide);
            } catch (Exception e) {
                log.error("Unable to create: {} because: {}", guide, e.getMessage(), e);
            }
        }
        return guides;
    }

    @Override
    public List<TourBind> createTourBindings(List<Tour> tours, List<Employee> employees) throws PicassoException {
        log.info("start create bindings");
        List<TourBind> bindings = new ArrayList<>();

        List<Driver> drivers = employees.stream()
                .filter(e -> Employee.Type.DRIVER.equals(e.getType()))
                .map(Driver.class::cast)
                .toList();

        List<Guide> guides = employees.stream()
                .filter(e -> Employee.Type.GUIDE.equals(e.getType()))
                .map(Guide.class::cast)
                .toList();

        int totalTours = tours.size();
        int totalGuides = guides.size();
        int totalDrivers = drivers.size();

        for (int i = 0; i < BINDINGS_COUNT_TO_CREATE; i++) {
            Tour tour = tours.get(RandomUtils.nextInt(0, totalTours - 1));
            Employee driver = drivers.get(RandomUtils.nextInt(0, totalDrivers - 1));
            Employee guide = guides.get(RandomUtils.nextInt(0, totalGuides - 1));

            try {
                int daysCount = tour.getDaysCount();
                int rndDaysCount = RandomUtils.nextInt(1, daysCount == 0 ? 1 : daysCount);

                LocalDateTime startDate = dateToLocalDateTime(faker.date().between(localDateTimeToDate(tour.getStartDate()), localDateTimeToDate(tour.getEndDate())));

                LocalDateTime endDate = startDate.plusDays(rndDaysCount);

                if (endDate.isAfter(tour.getEndDate()))
                    endDate = tour.getEndDate();
                TourBind tourBindDriver = new TourBind(driver, tour, startDate, endDate);
                TourBind tourBindGuide = new TourBind(guide, tour, startDate, endDate);
                TourBind save1 = tourBindService.save(tourBindDriver);
                log.debug("created new {}", save1);
                TourBind save2 = tourBindService.save(tourBindGuide);
                log.debug("created new {}", save2);
                bindings.add(save1);
                bindings.add(save2);
            } catch (BindException e) {
                log.error(e.getMessage(), e);
            } catch (Exception e) {
                log.error("Something went wrong while creating bindings. {}", e.getMessage(), e);
                throw e;
            }
        }
        return bindings;
    }
}
