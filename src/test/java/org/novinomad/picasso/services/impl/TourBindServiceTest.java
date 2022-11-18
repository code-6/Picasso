package org.novinomad.picasso.services.impl;

import org.junit.jupiter.api.*;
import org.novinomad.picasso.erm.dto.bind.TourBindModel;
import org.novinomad.picasso.erm.entities.Driver;
import org.novinomad.picasso.erm.entities.Guide;
import org.novinomad.picasso.erm.entities.Tour;
import org.novinomad.picasso.erm.entities.TourParticipant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = {"classpath:application.yml", "classpath:application-test.yml"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TourBindServiceTest {

    @Autowired
    private TourBindService tourBindService;

    Tour tour1 = new Tour();
    Tour tour2 = new Tour();

    TourParticipant guide1 = new Guide("test guide 1");
    TourParticipant guide2 = new Guide("test guide 2");
    TourParticipant driver1 = new Driver("test driver 1");
    TourParticipant driver2 = new Driver("test driver 2");

    LocalDateTime tour1Start = LocalDateTime.of(2022, 10, 1, 9,35);
    LocalDateTime tour2Start = LocalDateTime.of(2022, 10, 5, 9,35);
    LocalDateTime tour1End = LocalDateTime.of(2022, 10, 10, 9,15);
    LocalDateTime tour2End = LocalDateTime.of(2022, 10, 17, 23,10);

    @PostConstruct
    void init() {
        tour1.setId(1L);
        tour1.setName("test tour 1");
        tour1.setStartDate(tour1Start);
        tour1.setEndDate(tour1End);

        tour2.setId(2L);
        tour2.setName("test tour 2");
        tour2.setStartDate(tour2Start);
        tour2.setEndDate(tour2End);

        guide1.setId(1L);
        guide2.setId(2L);

        driver1.setId(1L);
        driver2.setId(2L);
    }


    @Test
    void testSaveBind() {

        TourBindModel tourBindModel = new TourBindModel();
        tourBindModel.setTour(tour1);

    }
}