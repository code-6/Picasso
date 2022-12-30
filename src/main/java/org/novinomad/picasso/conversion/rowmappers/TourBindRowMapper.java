package org.novinomad.picasso.conversion.rowmappers;

import lombok.RequiredArgsConstructor;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.commons.utils.CommonDateUtils;
import org.novinomad.picasso.domain.erm.entities.tour.TourBind;
import org.novinomad.picasso.services.tour.TourService;
import org.novinomad.picasso.services.tour_participants.TourParticipantService;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class TourBindRowMapper implements RowMapper<TourBind> {


    private final TourParticipantService tourParticipantService;


    private final TourService tourService;

    @Override
    public TourBind mapRow(ResultSet rs, int rowNum) throws SQLException {
        TourBind tourBind = new TourBind();
        tourBind.setId(rs.getLong("id"));
        LocalDateTime startDate = rs.getTimestamp("start_date").toLocalDateTime();
        LocalDateTime endDate = rs.getTimestamp("end_date").toLocalDateTime();
        tourBind.setDateTimeRange(new LocalDateTimeRange(startDate, endDate));
        tourBind.setCreateDate(rs.getTimestamp("create_date").toLocalDateTime());
        Timestamp lastUpdateDate = rs.getTimestamp("last_update_date");
        tourBind.setLastUpdateDate(lastUpdateDate != null ? lastUpdateDate.toLocalDateTime() : null);
        tourBind.setCreatedBy(rs.getString("created_by"));
        tourBind.setLastUpdatedBy(rs.getString("last_updated_by"));
        tourBind.setDeleted(rs.getBoolean("deleted"));
        try {
            tourBind.setTour(tourService.getById(rs.getLong("tour_id")).orElseThrow(NoSuchElementException::new));
            tourBind.setTourParticipant(tourParticipantService.getById(rs.getLong("tour_participant_id")).orElse(null));
        } catch (NoSuchElementException e) {
//            log.error("failed to map tour bind row because unable to fetch tour or participant data. {}", e.getMessage(), e);
            throw new CommonRuntimeException(e);
        }
        return tourBind;
    }
}
