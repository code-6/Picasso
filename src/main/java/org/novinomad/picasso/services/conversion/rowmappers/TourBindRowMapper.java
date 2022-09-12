package org.novinomad.picasso.services.conversion.rowmappers;

import org.novinomad.picasso.commons.utils.CommonDateUtils;
import org.novinomad.picasso.entities.domain.impl.TourBind;
import org.novinomad.picasso.services.IEmployeeService;
import org.novinomad.picasso.services.ITourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@Component
public class TourBindRowMapper implements RowMapper<TourBind> {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private ITourService tourService;

    @Override
    public TourBind mapRow(ResultSet rs, int rowNum) throws SQLException {
        TourBind tourBind = new TourBind();
        tourBind.setId(rs.getLong("id"));
        tourBind.setEndDate(CommonDateUtils.dateToLocalDateTime(rs.getTimestamp("end_date")));
        tourBind.setStartDate(CommonDateUtils.dateToLocalDateTime(rs.getTimestamp("start_date")));
        tourBind.setTour(tourService.get(rs.getLong("tour_id")).orElseThrow(NoSuchElementException::new));
        tourBind.setEmployee(employeeService.get(rs.getLong("employee_id")).orElseThrow(NoSuchElementException::new));

        return tourBind;
    }
}
