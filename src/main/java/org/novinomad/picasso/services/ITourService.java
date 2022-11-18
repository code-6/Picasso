package org.novinomad.picasso.services;

import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.erm.dto.filters.TourFilter;
import org.novinomad.picasso.erm.entities.Tour;
import org.novinomad.picasso.commons.exceptions.StorageException;
import org.novinomad.picasso.commons.exceptions.base.CommonException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ITourService extends ICrud<Long, Tour> {
    List<Tour> get(TourFilter tourFilter);

    Tour save(Tour tour, List<MultipartFile> files) throws CommonException;

    void deleteTourFile(Long tourId, String fileName) throws IOException, StorageException;

    Resource getTourFile(Long tourId, String fileName) throws FileNotFoundException, StorageException;
}
