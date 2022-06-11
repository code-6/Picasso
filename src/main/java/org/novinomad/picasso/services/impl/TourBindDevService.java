package org.novinomad.picasso.services.impl;

import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.novinomad.picasso.commons.utils.CarUtils;
import org.novinomad.picasso.domain.entities.impl.*;
import org.novinomad.picasso.exceptions.TourBindException;
import org.novinomad.picasso.repositories.DriverRepository;
import org.novinomad.picasso.repositories.GuideRepository;
import org.novinomad.picasso.repositories.TourBindRepository;
import org.novinomad.picasso.repositories.TourRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.novinomad.picasso.commons.utils.DateUtils.dateToLocalDateTime;
import static org.novinomad.picasso.commons.utils.DateUtils.localDateTimeToDate;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Profile(value = {"dev", "test"})
public class TourBindDevService extends TourBindService {

    static final LocalDate CURRENT_DATE = LocalDate.now();
    static final int CURRENT_YEAR = CURRENT_DATE.getYear();
    static final int CURRENT_MONTH = CURRENT_DATE.getYear();
    static final int PREV_MONTH = CURRENT_MONTH - 1;
    static final int NEXT_MONTH = CURRENT_MONTH + 1;
    static final int NEXT_MONTH_MAX_DAY = CURRENT_DATE.plusMonths(1).lengthOfMonth();
    static final int EMPLOYEES_COUNT_TO_CREATE = 10;
    static final int TOURS_COUNT_TO_CREATE = 15;
    static final int BINDINGS_COUNT_TO_CREATE = 60;
    static final int MAX_TOUR_DAYS_COUNT = 15;
    static final int MIN_TOUR_DAYS_COUNT = 3;
    static final Date minDate;
    static final Date maxDate;

    static {
        Calendar instance = Calendar.getInstance();
        instance.set(CURRENT_YEAR, PREV_MONTH, 1, 0, 0, 0);

        minDate = instance.getTime();

        instance.set(CURRENT_YEAR, NEXT_MONTH + 1, 1, 0, 0, 0);

        maxDate = instance.getTime();
    }

    final Faker faker;
    final GuideRepository guideRepository;
    final DriverRepository driverRepository;
    final TourRepository tourRepository;

    public TourBindDevService(TourBindRepository tourBindRepository,
                              Faker faker,
                              GuideRepository guideRepository,
                              DriverRepository driverRepository,
                              TourRepository tourRepository) {
        super(tourBindRepository);
        this.faker = faker;
        this.guideRepository = guideRepository;
        this.driverRepository = driverRepository;
        this.tourRepository = tourRepository;
    }


    @PostConstruct
    void init() {
        log.info("start initialize DB");
        createBindings();
        log.info("end initialize DB");
    }

    List<Guide> createGuides() {
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

    List<Driver> createDrivers() {
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

    List<Tour> createTours() {
        log.info("start create tours");
        List<Tour> tours = new ArrayList<>();
        for (int i = 0; i < TOURS_COUNT_TO_CREATE; i++) {
            String name = faker.funnyName().name();
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

    List<TourBind> createBindings() {
        log.info("start create bindings");
        List<TourBind> bindings = new ArrayList<>();
        List<Tour> tours = createTours();
        List<Driver> drivers = createDrivers();
        List<Guide> guides = createGuides();

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
                TourBind save1 = getTourBindRepository().save(tourBindDriver);
                log.debug("created new {}", save1);
                TourBind save2 = getTourBindRepository().save(tourBindGuide);
                log.debug("created new {}", save2);
                bindings.add(save1);
                bindings.add(save2);
            } catch (TourBindException e) {
                log.error(e.getMessage(), e);
            } catch (Exception e) {
                log.error("Something went wrong while creating bindings. {}", e.getMessage(), e);
                throw e;
            }
        }
        return bindings;
    }

}
