package org.novinomad.picasso.services.tour;

import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.commons.exceptions.BindException;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.conversion.rowmappers.TourBindRowMapper;
import org.novinomad.picasso.domain.dto.tour.filters.TourBindFilter;
import org.novinomad.picasso.domain.dto.tour.gantt.Task;
import org.novinomad.picasso.domain.erm.entities.tour.Tour;
import org.novinomad.picasso.domain.erm.entities.tour.TourBind;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;
import org.novinomad.picasso.repositories.jpa.TourBindJpaRepository;
import org.novinomad.picasso.services.AbstractCrudCacheService;
import org.novinomad.picasso.services.tour_participants.TourParticipantService;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.novinomad.picasso.commons.utils.CommonDateUtils.findOverlappedRange;

@Service
public class TourBindServiceImpl extends AbstractCrudCacheService<Long, TourBind> implements TourBindService {

    private final TourBindJpaRepository tourBindRepository;

    private final TourService tourService;

    private final TourParticipantService tourParticipantService;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final TourBindRowMapper tourBindRowMapper;

    protected TourBindServiceImpl(TourBindJpaRepository tourBindRepository,
                                  TourService tourService,
                                  TourParticipantService tourParticipantService,
                                  NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                  TourBindRowMapper tourBindRowMapper) {
        super(tourBindRepository);
        this.tourBindRepository = tourBindRepository;
        this.tourService = tourService;
        this.tourParticipantService = tourParticipantService;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.tourBindRowMapper = tourBindRowMapper;
    }

    @Override
    public TourBind bind(TourParticipant tourParticipant, Tour tour, LocalDateTimeRange dateRange) throws BindException {
        TourBind tourBind = new TourBind(tourParticipant, tour, dateRange);

        validateBind(tourBind);

        return save(tourBind);
    }

    @Override
    public TourBind bind(Long tourParticipantID, Long tourId, LocalDateTimeRange dateRange) throws BindException {

        TourParticipant tourParticipant = tourParticipantService.getByIdOrElseThrow(tourParticipantID);

        Tour tour = tourService.getByIdOrElseThrow(tourId);

        return bind(tourParticipant, tour, dateRange);
    }

    @Override
    public void validateBind(TourBind tourBind) throws BindException {
        TourParticipant tourParticipant = tourBind.getTourParticipant();

        List<TourBind> overlapsBinds = tourBindRepository.findOverlappedBinds(tourBind.getTour().getId(), tourParticipant.getId(),
                tourBind.getStartDate(), tourBind.getEndDate());

        if (!overlapsBinds.isEmpty()) {
            Map<Tour, LocalDateTimeRange> overlapsToursAndRanges = overlapsBinds.stream()
                    .collect(
                            Collectors.toMap(TourBind::getTour,
                                    tb -> findOverlappedRange(tourBind.getDateTimeRange(), tb.getDateTimeRange()))
                    );
            throw new BindException(tourParticipant, tourBind.getTour(), tourBind.getDateTimeRange(), overlapsToursAndRanges);
        }
    }

    @Override
    public void validateBind(Long tourId, Long tourParticipantId, LocalDateTimeRange bindRange) throws CommonRuntimeException, BindException {
        List<TourBind> overlapsBinds = tourBindRepository.findOverlappedBinds(tourId, tourParticipantId, bindRange.getStartDate(), bindRange.getEndDate());

        if (!overlapsBinds.isEmpty()) {
            Map<Tour, LocalDateTimeRange> overlapsToursAndRanges = overlapsBinds.stream()
                    .collect(Collectors.toMap(TourBind::getTour, tb -> findOverlappedRange(bindRange, tb.getDateTimeRange())));

            TourParticipant tourParticipant = tourParticipantService.getById(tourParticipantId)
                    .orElseThrow(() -> new CommonRuntimeException("TourParticipant with id: {} not found in DB", tourParticipantId));

            Tour tour = tourService.getById(tourId)
                    .orElseThrow(() -> new CommonRuntimeException("Tour with id: {} not found in DB", tourId));

            throw new BindException(tourParticipant, tour, bindRange, overlapsToursAndRanges);
        }
    }

    @Override
    public List<TourBind> findOverlappedBinds(Long tourId, Long tourParticipantId, LocalDateTimeRange newBindDateRange) {
        return tourBindRepository.findOverlappedBinds(tourId, tourParticipantId, newBindDateRange.getStartDate(), newBindDateRange.getEndDate());
    }

    @Override
    public List<TourBind> get(TourBindFilter tourBindFilter) {
        return get().parallelStream()
                .filter(tourBindFilter.getStartDate() != null
                        ? t -> t.getStartDate().isAfter(tourBindFilter.getStartDate()) || t.getStartDate().isEqual(tourBindFilter.getStartDate())
                        : t -> true)
                .filter(tourBindFilter.getEndDate() != null
                        ? t -> t.getEndDate().isBefore(tourBindFilter.getEndDate()) || t.getEndDate().isEqual(tourBindFilter.getEndDate())
                        : t -> true)
                .filter(!CollectionUtils.isEmpty(tourBindFilter.getTourIds())
                        ? t -> tourBindFilter.getTourIds().contains(t.getId())
                        : t -> true)
                .filter(!CollectionUtils.isEmpty(tourBindFilter.getTourParticipantIds())
                        ? t -> t.getTourParticipant() == null || tourBindFilter.getTourParticipantIds().contains(t.getTourParticipant().getId())
                        : t -> true)
                .filter(tb -> !tb.getDeleted())
                .sorted()
                .toList();

//        StringBuilder sql = new StringBuilder("""
//                select tb.id, tb.tour_id, tb.tour_participant_id, tb.start_date, tb.end_date, tb.CREATE_DATE, tb.CREATED_BY, tb.DELETED, tb.LAST_UPDATE_DATE, tb.LAST_UPDATED_BY
//                from TOUR_BIND tb
//                join TOUR t on tb.tour_id = t.id
//                left join TOUR_PARTICIPANT e on tb.TOUR_PARTICIPANT_ID = e.ID
//                where (:endDate is null or t.start_date <= :endDate)
//                and (:startDate is null or t.end_date >= :startDate)
//                """);
//
//        MapSqlParameterSource params = new MapSqlParameterSource()
//                .addValue("startDate", localDateTimeToDate(tourBindFilter.getStartDate()))
//                .addValue("endDate", localDateTimeToDate(tourBindFilter.getEndDate()));
//
//        if (!CollectionUtils.isEmpty(tourBindFilter.getTourIds())) {
//            sql.append(" and (t.id in (:tourIds)) ");
//            params.addValue("tourIds", tourBindFilter.getTourIds());
//        }
//
//        if (!CollectionUtils.isEmpty(tourBindFilter.getTourParticipantIds())) {
//            sql.append(" and (e.id in (:tourParticipantIds)) ");
//            params.addValue("tourParticipantIds", tourBindFilter.getTourParticipantIds());
//        }
//
//        sql.append("""
//                group by tb.id, tb.tour_id, tb.tour_participant_id, tb.start_date, tb.end_date
//                order by t.START_DATE, tb.START_DATE, t.END_DATE, tb.END_DATE
//                """);
//
//        return namedParameterJdbcTemplate.query(sql.toString(), params, tourBindRowMapper);
    }

    @Override
    public List<Task> getForGanttChart(TourBindFilter tourBindFilter) {
        if (tourBindFilter == null)
            tourBindFilter = new TourBindFilter();

        return Task.fromBindsWithChildrenInList(get(tourBindFilter));
    }
}
