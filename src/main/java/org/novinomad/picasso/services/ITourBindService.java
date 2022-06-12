package org.novinomad.picasso.services;

import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.novinomad.picasso.exceptions.TourBindException;
import org.novinomad.picasso.exceptions.base.PicassoException;

import java.time.LocalDateTime;

public interface ITourBindService extends ICrud<TourBind> {

    TourBind bind(Employee employee, Tour tour, LocalDateTime startDate, LocalDateTime endDate) throws TourBindException;
    TourBind bind(Long employeeID, Long tourId, LocalDateTime startDate, LocalDateTime endDate) throws PicassoException;
}
