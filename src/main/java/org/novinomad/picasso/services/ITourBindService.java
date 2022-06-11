package org.novinomad.picasso.services;

import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.novinomad.picasso.exceptions.TourBindException;

import java.time.LocalDateTime;

public interface ITourBindService {
    void bind(Employee employee, Tour tour, LocalDateTime startDate, LocalDateTime endDate) throws TourBindException;

    boolean intersects(TourBind tourBind) throws TourBindException;
}
