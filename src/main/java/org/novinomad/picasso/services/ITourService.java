package org.novinomad.picasso.services;

import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.dto.filters.TourFilter;

import java.util.List;

public interface ITourService extends ICrud<Tour> {
    List<Tour> get(TourFilter tourFilter);
}
