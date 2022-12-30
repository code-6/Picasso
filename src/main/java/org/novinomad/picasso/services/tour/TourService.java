package org.novinomad.picasso.services.tour;

import org.novinomad.picasso.commons.Crud;
import org.novinomad.picasso.domain.dto.tour.filters.TourFilter;
import org.novinomad.picasso.domain.erm.entities.tour.Tour;
import org.novinomad.picasso.commons.exceptions.StorageException;
import org.novinomad.picasso.commons.exceptions.base.CommonException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface TourService extends Crud<Long, Tour> {
    List<Tour> get(TourFilter tourFilter);

    Tour save(Tour tour, List<MultipartFile> files) throws CommonException;

    void deleteTourFile(Long tourId, String fileName) throws IOException, StorageException;

    void deleteTourFile(Tour tour, String fileName) throws StorageException;

    Resource getTourFile(Long tourId, String fileName) throws FileNotFoundException, StorageException;
}
