package org.novinomad.picasso.services.conversion.rowmappers;

import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.commons.exceptions.base.CommonException;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.commons.utils.CommonDateUtils;
import org.novinomad.picasso.erm.entities.TourBind;
import org.novinomad.picasso.services.ITourParticipantService;
import org.novinomad.picasso.services.ITourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@Slf4j
@Component
public class TourBindRowMapper implements RowMapper<TourBind> {

    @Autowired
    private ITourParticipantService tourParticipantService;

    @Autowired
    private ITourService tourService;

    @Override
    public TourBind mapRow(ResultSet rs, int rowNum) throws SQLException {
        TourBind tourBind = new TourBind();
        tourBind.setId(rs.getLong("id"));
        tourBind.setEndDate(CommonDateUtils.dateToLocalDateTime(rs.getTimestamp("end_date")));
        tourBind.setStartDate(CommonDateUtils.dateToLocalDateTime(rs.getTimestamp("start_date")));
        try {
            tourBind.setTour(tourService.get(rs.getLong("tour_id")).orElseThrow(NoSuchElementException::new));
            tourBind.setTourParticipant(tourParticipantService.get(rs.getLong("tour_participant_id")).orElse(null));
        } catch (NoSuchElementException e) {
            log.error("failed to map tour bind row because unable to fetch tour or participant data. {}", e.getMessage(), e);
            throw new CommonRuntimeException(e);
        }
        return tourBind;
    }
}
