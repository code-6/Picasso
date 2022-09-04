package org.novinomad.picasso.services;

import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.dto.filters.TourFilter;
import org.novinomad.picasso.exceptions.base.PicassoException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ITourService extends ICrud<Tour> {
    List<Tour> get(TourFilter tourFilter);

    Tour save(Tour tour, List<MultipartFile> files) throws PicassoException;
}
