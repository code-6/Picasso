package org.novinomad.picasso.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.dto.filters.TourFilter;
import org.novinomad.picasso.exceptions.StorageException;
import org.novinomad.picasso.exceptions.base.PicassoException;
import org.novinomad.picasso.repositories.jpa.TourRepository;
import org.novinomad.picasso.services.ITourService;
import org.novinomad.picasso.services.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourService implements ITourService {

    final TourRepository tourRepository;

    final StorageService storageService;

    private static final String PATH_SEPARATOR = FileSystems.getDefault().getSeparator();
    private static final String DEFAULT_ROOT_DIR = "." + PATH_SEPARATOR + "toursFiles";

    private Path rootLocation;

    public String getRootLocation() {
        return rootLocation.toString() + PATH_SEPARATOR;
    }

    @Value("${tour-files-folder}")
    private String filesRootDir;

    /**
     * Makes sure that root folder exists.
     * If root folder doesn't exist then create new by default in folder from where application was started.
     */
    @PostConstruct
    public void initializeRootDirectoryForTourFiles() {

        // make sure that path to rot directory is provided, if not then use default value
        if (StringUtils.isBlank(filesRootDir)) filesRootDir = DEFAULT_ROOT_DIR;

        if (storageService.isValidPath(filesRootDir)){
            if (!storageService.existsFolder(filesRootDir)) {
                try {
                    rootLocation = storageService.createDir(filesRootDir);
                    log.info("root directory {} for tour files created successfully", rootLocation.getFileName());
                } catch (IOException | StorageException e) {
                    log.error("root directory {} for tour files not created because {}", filesRootDir, e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            } else {
                rootLocation = Paths.get(filesRootDir);
            }
        }
    }

    @Override
    public Tour save(Tour tour) throws PicassoException {
        try {
            Tour savedTour = tourRepository.save(tour);
            log.debug("saved {}", tour);
            return savedTour;
        } catch (Exception e) {
            log.error("unable to create: {} because: {}", tour, e.getMessage(), e);
            throw new PicassoException(e, "unable to create: {} because: {}", tour, e.getMessage());
        }
    }

    @Transactional
    public List<Tour> save(Collection<Tour> tours) {
        List<Tour> savedTours = new ArrayList<>();
        tours.forEach(tour -> {
            try {
                savedTours.add(save(tour));
            } catch (PicassoException ignored) {
                // ignored because save contains logging.
            }
        });
        if (savedTours.size() != tours.size())
            log.warn("not all Tours are saved. To be saved: {} saved: {}", tours.size(), savedTours.size());

        return savedTours;
    }

    @Override
    public void delete(Long id) throws PicassoException {
        try {
            tourRepository.deleteById(id);
        } catch (Exception e) {
            log.error("unable to delete Tour with id: {} because: {}", id, e.getMessage(), e);
            throw new PicassoException(e, "unable to delete Tour with id: {} because: {}", id, e.getMessage());
        }
    }

    @Transactional
    public List<Long> delete(Collection<Long> ids) {
        List<Long> deletedTours = new ArrayList<>();

        ids.forEach(id -> {
            try {
                delete(id);
                deletedTours.add(id);
            } catch (PicassoException ignored) {
                // ignored because save contains logging.
            }
        });
        if (deletedTours.size() != ids.size())
            log.warn("not all Tours are deleted. To be deleted: {} deleted: {}", deletedTours.size(), ids.size());

        return deletedTours;
    }

    @Transactional
    public List<Long> deleteAll(Collection<Tour> tours) {
        List<Long> ids = tours.stream().map(Tour::getId).toList();
        return delete(ids);
    }

    @Override
    public Optional<Tour> get(Long id) {
        try {
            return tourRepository.findById(id);
        } catch (Exception e) {
            log.error("unable to get Tour by id: {} because: {}", id, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public List<Tour> get() {
        return tourRepository.findAll();
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
    public Tour save(Tour tour, List<MultipartFile> files) throws PicassoException {
        tour.addFile(files);
        tour = tourRepository.save(tour);
        String newDir = rootLocation.toString() + PATH_SEPARATOR + tour.getId();
        try {
            if(!storageService.existsFolder(newDir))
                storageService.createDir(newDir);

            for (MultipartFile file : files)
                storageService.store(file, newDir);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return tour;
    }

    @Override
    public void deleteTourFile(Long tourId, String fileName) throws IOException {
        String filePath = rootLocation.toString() + PATH_SEPARATOR + tourId + PATH_SEPARATOR + fileName;
        try {
            storageService.delete(filePath);
        } catch (Exception e) {
            log.error("unable to delete tour file {} because {}", filePath, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Resource getTourFile(Long tourId, String fileName) throws FileNotFoundException, StorageException {
        String filePath = rootLocation.toString() + PATH_SEPARATOR + tourId + PATH_SEPARATOR + fileName;
        return storageService.loadAsResource(filePath);
    }
}
