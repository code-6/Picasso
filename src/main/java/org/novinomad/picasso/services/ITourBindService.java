package org.novinomad.picasso.services;

import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.novinomad.picasso.dto.TourBindDTO;
import org.novinomad.picasso.dto.filters.TourBindFilter;
import org.novinomad.picasso.exceptions.base.PicassoException;

import java.time.LocalDateTime;
import java.util.List;

public interface ITourBindService extends ICrud<TourBind> {

    TourBind bind(Employee employee, Tour tour, LocalDateTime startDate, LocalDateTime endDate) throws PicassoException;
    TourBind bind(Long employeeID, Long tourId, LocalDateTime startDate, LocalDateTime endDate) throws PicassoException;
    List<TourBind> get(TourBindFilter tourBindFilter);
}
