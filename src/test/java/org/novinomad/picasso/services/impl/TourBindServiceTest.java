package org.novinomad.picasso.services.impl;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.novinomad.picasso.entities.domain.impl.Driver;
import org.novinomad.picasso.entities.domain.impl.Guide;
import org.novinomad.picasso.entities.domain.impl.Tour;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

//@SpringBootTest
//@ActiveProfiles("test")
//@TestPropertySource(locations = "classpath:application-test.yml")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TourBindServiceTest {

    //    @Autowired
//    TourRepository tourRepository;
//
//    @Autowired
//    DriverRepository driverRepository;
//
//    @Autowired
//    GuideRepository guideRepository;
//
//    @Autowired
//    TourBindService tourBindService;
//
//    @Autowired
    private static final Faker FAKER = new Faker();

    private static final Map<Long, Tour> TOURS = new HashMap<>();
    private static final Map<Long, Driver> DRIVERS = new HashMap<>();
    private static final Map<Long, Guide> GUIDES = new HashMap<>();

    private static final AtomicLong ID = new AtomicLong();

    static {
        LocalDateTime sd1 = LocalDateTime.of(2022, Calendar.JANUARY, 1, 0, 0);
        LocalDateTime ed1 = LocalDateTime.of(2022, Calendar.JANUARY, 5, 0, 0);
        TOURS.put(ID.incrementAndGet(), new Tour(ID.get(), FAKER.funnyName().name(), sd1, ed1));

        LocalDateTime sd2 = LocalDateTime.of(2022, Calendar.JANUARY, 6, 0, 0);
        LocalDateTime ed2 = LocalDateTime.of(2022, Calendar.JANUARY, 10, 0, 0);
        TOURS.put(ID.incrementAndGet(), new Tour(ID.get(), FAKER.funnyName().name(), sd2, ed2));

        LocalDateTime sd3 = LocalDateTime.of(2022, Calendar.JANUARY, 10, 0, 0);
        LocalDateTime ed3 = LocalDateTime.of(2022, Calendar.JANUARY, 15, 0, 0);
        TOURS.put(ID.incrementAndGet(), new Tour(ID.get(), FAKER.funnyName().name(), sd3, ed3));

        LocalDateTime sd4 = LocalDateTime.of(2022, Calendar.JANUARY, 12, 0, 0);
        LocalDateTime ed4 = LocalDateTime.of(2022, Calendar.JANUARY, 18, 0, 0);
        TOURS.put(ID.incrementAndGet(), new Tour(ID.get(), FAKER.funnyName().name(), sd4, ed4));

        LocalDateTime sd5 = LocalDateTime.of(2022, Calendar.JANUARY, 13, 0, 0);
        LocalDateTime ed5 = LocalDateTime.of(2022, Calendar.JANUARY, 20, 0, 0);
        TOURS.put(ID.incrementAndGet(), new Tour(ID.get(), FAKER.funnyName().name(), sd5, ed5));

        LocalDateTime sd6 = LocalDateTime.of(2022, Calendar.JANUARY, 18, 0, 0);
        LocalDateTime ed6 = LocalDateTime.of(2022, Calendar.JANUARY, 23, 0, 0);
        TOURS.put(ID.incrementAndGet(), new Tour(ID.get(), FAKER.funnyName().name(), sd6, ed6));

        LocalDateTime sd7 = LocalDateTime.of(2022, Calendar.JANUARY, 23, 0, 0);
        LocalDateTime ed7 = LocalDateTime.of(2022, Calendar.JANUARY, 27, 0, 0);
        TOURS.put(ID.incrementAndGet(), new Tour(ID.get(), FAKER.funnyName().name(), sd7, ed7));

        LocalDateTime sd8 = LocalDateTime.of(2022, Calendar.JANUARY, 28, 0, 0);
        LocalDateTime ed8 = LocalDateTime.of(2022, Calendar.FEBRUARY, 2, 0, 0);
        TOURS.put(ID.incrementAndGet(), new Tour(ID.get(), FAKER.funnyName().name(), sd8, ed8));

        DRIVERS.put(ID.getAndIncrement(), new Driver(ID.get(), "Driver_1"));
        DRIVERS.put(ID.getAndIncrement(), new Driver(ID.get(), "Driver_2"));

        GUIDES.put(ID.getAndIncrement(), new Guide(ID.get(), "Guide_1"));
        GUIDES.put(ID.getAndIncrement(), new Guide(ID.get(), "Guide_2"));
    }

    @Test
    void intersects() {

    }
}