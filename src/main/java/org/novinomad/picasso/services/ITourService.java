package org.novinomad.picasso.services;

import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.dto.filters.TourFilter;
import org.novinomad.picasso.entities.domain.impl.Tour;
import org.novinomad.picasso.exceptions.StorageException;
import org.novinomad.picasso.exceptions.base.PicassoException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ITourService extends ICrud<Tour> {
    List<Tour> get(TourFilter tourFilter);

    Tour save(Tour tour, List<MultipartFile> files) throws PicassoException;

    void deleteTourFile(Long tourId, String fileName) throws IOException;

    Resource getTourFile(Long tourId, String fileName) throws FileNotFoundException, StorageException;
}
