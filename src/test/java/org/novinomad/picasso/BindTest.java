package org.novinomad.picasso;

import org.junit.jupiter.api.*;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.domain.dto.tour.bind.TourBindModel;
import org.novinomad.picasso.commons.exceptions.BindException;
import org.novinomad.picasso.domain.erm.entities.tour.Tour;
import org.novinomad.picasso.domain.erm.entities.tour.TourBind;
import org.novinomad.picasso.domain.erm.entities.tour_participants.Driver;
import org.novinomad.picasso.domain.erm.entities.tour_participants.Guide;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;
import org.novinomad.picasso.services.tour.TourBindService;
import org.novinomad.picasso.services.tour_participants.TourParticipantService;
import org.novinomad.picasso.services.tour.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = {"classpath:application.yml"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BindTest {

    @Autowired
    private TourBindService tourBindService;

    @Autowired
    private TourParticipantService participantService;

    @Autowired
    private TourService tourService;

    // initialize test data.
    private TourParticipant guide;
    private TourParticipant driver;

    private LocalDateTime tour1StartDate;
    private LocalDateTime tour1EndDate;

    private LocalDateTime tour2StartDate;
    private LocalDateTime tour2EndDate;
    private Tour tour1;
    private Tour tour2;

    private List<TourBind> tour1Binds;

    @PostConstruct
    public void init() {
        tour1StartDate = LocalDateTime.of(2022,10, 1, 10, 25);
        tour1EndDate = LocalDateTime.of(2022,10, 10, 14, 5);

        tour2StartDate = LocalDateTime.of(2022,10, 5, 9, 15);
        tour2EndDate = LocalDateTime.of(2022,10, 15, 20, 45);

        assertDoesNotThrow(() -> {
            guide = participantService.save(new Guide("guide"));
            driver = participantService.save(new Driver("driver"));

            tour1 = tourService.save(new Tour("First tour", tour1StartDate, tour1EndDate));
            tour2 = tourService.save(new Tour("Second tour", tour2StartDate, tour2EndDate));
        });
    }

    /**
     * Test case 1.
     * Regular case with one tour, one guide and one driver bound. Intersections or exceptions not expected.
     * Tour  from 2022-10-01 10:25:00 to 2022-10-10 14:05:00
     * Guide from 2022-10-02 08:30:00 to 2022-10-7 18:05:00
     * */
    @Test
    @Order(1)
    void testCreateBinds() {
        TourBindModel tourBindModel = new TourBindModel(tour1)
                .bindTourParticipant(guide, new LocalDateTimeRange(
                        LocalDateTime.of(2022, 10, 2, 8, 30),
                        LocalDateTime.of(2022,10, 7, 18, 0)));

        assertDoesNotThrow(() -> {
            tour1Binds = tourBindService.save(tourBindModel.toEntities());
            assertFalse(tour1Binds.isEmpty());
            assertEquals(1, tour1Binds.size());
            tour1Binds.forEach(b -> assertNotNull(b.getId()));
        });
    }

    /**
     * Test case 2.
     * Try to bind same guide to overlapped tour. Expected bind exception.
     * Tour2      from 2022-10-05 9:15:00 to 2022-10-15 20:45:00
     * Guide      from 2022-10-05 9:15:00 to 2022-10-15 20:45:00
     * */
    @Test
    @Order(2)
    void testCreateBindsExpectedBindExceptionBecauseOverlapped() {
        TourBindModel tourBindModel = new TourBindModel(tour2)
                .bindTourParticipant(guide, new LocalDateTimeRange(tour2StartDate, tour2EndDate));

        BindException e = assertThrows(BindException.class, () -> {
            tour1Binds = tourBindService.save(tourBindModel.toEntities());
        });
        assertNotNull(e.getOverlapsToursAndRanges());
        assertFalse(e.getOverlapsToursAndRanges().isEmpty());
        assertTrue(e.getOverlapsToursAndRanges().containsKey(tour1));
        assertNotNull(e.getOverlapsToursAndRanges().get(tour1));
        assertEquals(new LocalDateTimeRange(tour2StartDate, tour1EndDate), e.getOverlapsToursAndRanges().get(tour1));
    }
}
