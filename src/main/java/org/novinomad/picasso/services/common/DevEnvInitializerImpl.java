package org.novinomad.picasso.services.common;

import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.commons.exceptions.BindException;
import org.novinomad.picasso.commons.exceptions.base.CommonException;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.commons.utils.CarUtils;
import org.novinomad.picasso.commons.utils.CommonDateUtils;
import org.novinomad.picasso.commons.utils.PasswordUtil;
import org.novinomad.picasso.domain.erm.entities.common.AppSettings;
import org.novinomad.picasso.domain.erm.entities.auth.User;
import org.novinomad.picasso.domain.erm.entities.tour.Tour;
import org.novinomad.picasso.domain.erm.entities.tour.TourBind;
import org.novinomad.picasso.domain.erm.entities.tour_participants.Driver;
import org.novinomad.picasso.domain.erm.entities.tour_participants.Guide;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;
import org.novinomad.picasso.repositories.jpa.*;
import org.novinomad.picasso.services.tour.TourBindServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
@ConditionalOnProperty(name = "initialize-test-data", havingValue = "true")
@DependsOn("userServiceImpl")
public class DevEnvInitializerImpl implements DevEnvInitializer {
    static final LocalDate CURRENT_DATE = LocalDate.now();
    static final int NEXT_MONTH_MAX_DAY = CURRENT_DATE.plusMonths(1).lengthOfMonth();
    static final int TOUR_PARTICIPANTS_COUNT_TO_CREATE = 10;
    static final int TOURS_COUNT_TO_CREATE = 100;
    static final int BINDINGS_COUNT_TO_CREATE = 60;
    static final int MAX_TOUR_DAYS_COUNT = 20;
    static final int MIN_TOUR_DAYS_COUNT = 10;
    static final int MAX_USERS_COUNT = 10;
    static final Date minDate;
    static final Date maxDate;

    static final String CREATED_BY = "SYSTEM";

    static {
        minDate = CommonDateUtils.localDateTimeToDate(CURRENT_DATE.minusMonths(1).atStartOfDay());
        maxDate = CommonDateUtils.localDateTimeToDate(CURRENT_DATE.plusMonths(2).atStartOfDay());
    }

    final Faker faker;
    final GuideRepository guideRepository;
    final DriverRepository driverRepository;
    final TourRepository tourRepository;
    final TourBindServiceImpl tourBindService;

    final AppSettingsRepository appSettingsRepository;

    final UserRepository userRepository;

    @PostConstruct
    void init() {
        log.info("initialize DB with test data");

        try {
            Optional<AppSettings> setting = appSettingsRepository.findById(AppSettings.TEST_DATA_INITIALIZED.getId());

            boolean shouldInitialize = true;

            if(setting.isPresent()) {
                shouldInitialize = !Boolean.parseBoolean(setting.get().getSettingValue());
            }
            if(shouldInitialize) {
                List<TourParticipant> tourParticipants = null;
                try {
                    tourParticipants = createTourParticipants();
                } catch (Exception e) {
                    log.error("unable to create tour participants because {}", e.getMessage(), e);
                }
                List<Tour> tours = null;
                try {
                    tours = createTours();
                } catch (Exception e) {
                    log.error("unable to create tours because {}", e.getMessage(), e);
                }
                if(!CollectionUtils.isEmpty(tourParticipants) && !CollectionUtils.isEmpty(tours)) {
                    try {
                        createTourBindings(tours, tourParticipants);
                    } catch (Exception e) {
                        log.error("unable to create bindings because {}", e.getMessage(), e);
                    }
                }
                try {
                    createUsers();
                } catch (Exception e) {
                    log.error("unable to create users because {} ", e.getMessage(), e);
                }
                appSettingsRepository.save(AppSettings.TEST_DATA_INITIALIZED.value("true"));
            }
        } catch (Exception e) {
            log.error("unable to initialize DB with test data because: {}", e.getMessage());
        }
    }

    @Override
    public List<User> createUsers() {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < MAX_USERS_COUNT; i++) {
            User user = new User();
            user.setUsername(faker.name().username());
            user.setPassword(PasswordUtil.generateRandomPassword(9));
            user.addEmail(faker.internet().emailAddress());

            users.add(userRepository.save(user));
        }
        return users;
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

            List<String> files = new ArrayList<>();

            int rndFilesCount = RandomUtils.nextInt(0, 3);

            for (int j = 0; j < rndFilesCount; j++) {
                files.add(faker.file().fileName());
            }

            Tour tour = new Tour(name, desc, new LocalDateTimeRange(finalStartDate, finalEndDate), files);

            tour.setCreatedBy(CREATED_BY);
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

        for (int i = 0; i < TOUR_PARTICIPANTS_COUNT_TO_CREATE; i++) {
            final String driverName = faker.name().fullName();
            Driver driver = new Driver(driverName);
            driver.setCreatedBy(CREATED_BY);
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

        for (int i = 0; i < TOUR_PARTICIPANTS_COUNT_TO_CREATE; i++) {
            final String guideName = faker.name().fullName();
            Guide guide = new Guide(guideName);
            guide.setCreatedBy(CREATED_BY);
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
    public List<TourBind> createTourBindings(List<Tour> tours, List<TourParticipant> tourParticipants) throws CommonException {
        log.info("start create bindings");
        List<TourBind> bindings = new ArrayList<>();

        List<Driver> drivers = tourParticipants.stream()
                .filter(e -> TourParticipant.Type.DRIVER.equals(e.getType()))
                .map(Driver.class::cast)
                .toList();

        List<Guide> guides = tourParticipants.stream()
                .filter(e -> TourParticipant.Type.GUIDE.equals(e.getType()))
                .map(Guide.class::cast)
                .toList();

        int totalTours = tours.size();
        int totalGuides = guides.size();
        int totalDrivers = drivers.size();

        for (int i = 0; i < BINDINGS_COUNT_TO_CREATE; i++) {
            Tour tour = tours.get(RandomUtils.nextInt(0, totalTours - 1));
            TourParticipant driver = drivers.get(RandomUtils.nextInt(0, totalDrivers - 1));
            TourParticipant guide = guides.get(RandomUtils.nextInt(0, totalGuides - 1));

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
            } catch (BindException | CommonRuntimeException e) {
                log.warn(e.getMessage());
            }
        }
        return bindings;
    }
}
