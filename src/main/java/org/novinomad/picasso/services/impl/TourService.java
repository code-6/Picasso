package org.novinomad.picasso.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.novinomad.picasso.erm.dto.filters.TourFilter;
import org.novinomad.picasso.erm.entities.Tour;
import org.novinomad.picasso.commons.exceptions.StorageException;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.repositories.jpa.TourRepository;
import org.novinomad.picasso.services.ITourService;
import org.novinomad.picasso.services.StorageService;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.nio.file.*;
import java.util.*;

import static org.novinomad.picasso.services.StorageService.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourService implements ITourService {

    final TourRepository tourRepository;

    final StorageService storageService;
    private static final String DEFAULT_ROOT_DIR = "." + PATH_SEPARATOR + "toursFiles";

    private Path rootLocation;

    public String getRootLocation() {
        return rootLocation.toString() + PATH_SEPARATOR;
    }

    @Value("${app.tour-files-folder}")
    private String filesRootDir;

    /**
     * Makes sure that root folder exists.
     * If root folder doesn't exist then create new by default in folder from where application was started.
     */
    @PostConstruct
    public void initializeRootDirectoryForTourFiles() {

        // make sure that path to rot directory is provided, if not then use default value
        if (StringUtils.isBlank(filesRootDir)) filesRootDir = DEFAULT_ROOT_DIR;

        if (storageService.isValid(filesRootDir)){
            if (!storageService.exist(filesRootDir)) {
                try {
                    rootLocation = storageService.createFolder(filesRootDir);
                    log.info("root directory {} for tour files created successfully", rootLocation.getFileName());
                } catch (StorageException e) {
                    log.error("root directory {} for tour files not created because {}", filesRootDir, e.getMessage(), e);
                    throw new BeanCreationException("root directory "+filesRootDir+" for tour files not created because " + e.getMessage(), e);
                }
            } else {
                rootLocation = Paths.get(filesRootDir);
            }
        }
    }

    @Override
    public Tour save(Tour tour) throws CommonRuntimeException {
        try {
            return tourRepository.save(tour);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to create {} because {}", tour, e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            tourRepository.deleteById(id);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to delete tour with id {} because {}", id, e.getMessage());
        }
    }

    @Override
    public void deleteById(Iterable<Long> ids) throws CommonRuntimeException {
        try {
            tourRepository.deleteAllById(ids);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to delete tours with ids {} because {}", ids, e.getMessage());
        }
    }

    /**
     * @implNote If tours not provided wil be removed all items. So be careful when using this method
     * */
    @Override
    public void delete(Tour... tours) throws CommonRuntimeException {
        try {
            if(tours == null || tours.length == 0) {
                tourRepository.deleteAll();
            } else {
                tourRepository.deleteAll(Arrays.asList(tours));
            }
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to delete tours {} because {}", tours, e.getMessage());
        }
    }

    @Override
    public List<Tour> get(Long... ids) throws CommonRuntimeException {
        try {
            if(ids == null || ids.length == 0) {
                return tourRepository.findAll();
            } else {
                return tourRepository.findAllById(Arrays.asList(ids));
            }
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to get tours by ids {} because {}", ids, e.getMessage());
        }
    }

    @Override
    public Optional<Tour> get(Long id) throws CommonRuntimeException {
        try {
            return tourRepository.findById(id);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to get tour by id {} because {}", id, e.getMessage());
        }
    }

    @Override
    public List<Tour> get(TourFilter tourFilter) {
        try {
            return tourRepository.findByFilter(tourFilter.getStartDate(), tourFilter.getEndDate(), tourFilter.getTourIds().isEmpty() ? null : tourFilter.getTourIds());
        } catch (Exception e) {
            log.error("unable to get Tours by filter: {} because: {}", tourFilter, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Tour save(Tour tour, List<MultipartFile> newFiles) throws CommonRuntimeException {
        if(!CollectionUtils.isEmpty(newFiles))
            tour.addFile(newFiles);

        tour = tourRepository.save(tour);

        try {
            String tourFilesFolder = rootLocation.toString() + PATH_SEPARATOR + tour.getId() + PATH_SEPARATOR;

            if(!storageService.exist(tourFilesFolder))
                storageService.createFolder(tourFilesFolder);

            storageService.clearFolder(tourFilesFolder, tour.getFileNames().toArray(String[]::new));

            storageService.store(tourFilesFolder, newFiles);

        } catch (StorageException e) {
            log.error(e.getMessage(), e);
        }
        return tour;
    }

    @Override
    public void deleteTourFile(Long tourId, String fileName) throws StorageException {
        String filePath = rootLocation.toString() + PATH_SEPARATOR + tourId + PATH_SEPARATOR + fileName;
        storageService.delete(filePath);
    }

    @Override
    public Resource getTourFile(Long tourId, String fileName) throws StorageException {
        String filePath = rootLocation.toString() + PATH_SEPARATOR + tourId + PATH_SEPARATOR + fileName;
        return storageService.loadAsResource(filePath);
    }
}
