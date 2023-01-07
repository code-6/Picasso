package org.novinomad.picasso.services.tour;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.novinomad.picasso.commons.exceptions.StorageException;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.domain.dto.tour.filters.TourFilter;
import org.novinomad.picasso.domain.erm.entities.tour.Tour;
import org.novinomad.picasso.repositories.jpa.TourRepository;
import org.novinomad.picasso.services.AbstractCrudCacheService;
import org.novinomad.picasso.services.common.FileSystemStorageService;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.novinomad.picasso.services.common.FileSystemStorageService.PATH_SEPARATOR;


@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourServiceImpl extends AbstractCrudCacheService<Long, Tour> implements TourService {

    private final TourRepository tourRepository;

    private final FileSystemStorageService fileSystemStorageService;

    private static final String DEFAULT_ROOT_DIR = "." + PATH_SEPARATOR + "toursFiles";

    private Path rootLocation;

    @Value("${app.tour-files-folder}")
    private
    String filesRootDir;

    public TourServiceImpl(TourRepository tourRepository,
                           FileSystemStorageService fileSystemStorageService) {
        super(tourRepository);
        this.tourRepository = tourRepository;
        this.fileSystemStorageService = fileSystemStorageService;
    }

    /**
     * Makes sure that root folder exists.
     * If root folder doesn't exist then create new by default in folder from where application was started.
     */
    @PostConstruct
    public void initializeRootDirectoryForTourFiles() {

        // make sure that path to rot directory is provided, if not then use default value
        if (StringUtils.isBlank(filesRootDir)) filesRootDir = DEFAULT_ROOT_DIR;

        if (fileSystemStorageService.isValid(filesRootDir)){
            if (!fileSystemStorageService.exist(filesRootDir)) {
                try {
                    rootLocation = fileSystemStorageService.createFolder(filesRootDir);
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
    public void delete(Tour tour) {
        deleteAllTourFiles(tour);
        super.delete(tour);
    }

    @Override
    public void delete(Collection<Tour> tours) {
        tours.forEach(this::deleteAllTourFiles);
        super.delete(tours);
    }

    @Override
    public void deleteById(Long id) {
        getById(id).ifPresent(this::deleteAllTourFiles);
        super.deleteById(id);
    }

    @Override
    public void deleteById(Collection<Long> ids) {
        getById(ids).forEach(this::deleteAllTourFiles);
        super.deleteById(ids);
    }

    @Override
    public List<Tour> get() {
        return super.get().parallelStream().filter(t -> !t.getDeleted()).toList();
    }

    @Override
    public List<Tour> get(TourFilter tourFilter) {
        return get().stream()
                .filter(t -> !t.getDeleted()
                        && t.getDateTimeRange().between(tourFilter.getLocalDateTimeRange())
                        && (tourFilter.getTourIds().isEmpty() || tourFilter.getTourIds().contains(t.getId())))
                .sorted(Comparator.reverseOrder())
                .toList();

//        if(tourFilter.getStartDate() != null) {
//            stream = stream.filter(t -> t.getStartDate().isAfter(tourFilter.getStartDate()) || t.getStartDate().isEqual(tourFilter.getStartDate()));
//        }
//        if(tourFilter.getEndDate() != null) {
//            stream = stream.filter(t -> t.getEndDate().isBefore(tourFilter.getEndDate()) || t.getEndDate().isEqual(tourFilter.getEndDate()));
//        }
//        if(!CollectionUtils.isEmpty(tourFilter.getTourIds())) {
//            stream = stream.filter(t -> tourFilter.getTourIds().contains(t.getId()));
//        }
//        return stream.toList();
//        return tourRepository.findByFilter(tourFilter.getStartDate(), tourFilter.getEndDate(), tourFilter.getTourIds().isEmpty() ? null : tourFilter.getTourIds());
    }

    @Override
    public Tour save(Tour tour, List<MultipartFile> newFiles) throws CommonRuntimeException, StorageException {
        if(!CollectionUtils.isEmpty(newFiles)) {
            tour.addFile(newFiles);
        }

        tour = save(tour);

        String tourFilesFolder = rootLocation.toString() + PATH_SEPARATOR + tour.getId() + PATH_SEPARATOR;

        if(!fileSystemStorageService.exist(tourFilesFolder))
            fileSystemStorageService.createFolder(tourFilesFolder);

        fileSystemStorageService.clearFolder(tourFilesFolder, tour.getFileNames().toArray(String[]::new));

        fileSystemStorageService.store(tourFilesFolder, newFiles);

        return tour;
    }

    @Override
    public void deleteTourFile(Long tourId, String fileName) throws StorageException {
        String filePath = rootLocation.toString() + PATH_SEPARATOR + tourId + PATH_SEPARATOR + fileName;
        fileSystemStorageService.delete(filePath);
//        getById(tourId).ifPresent(tour -> {
//            tour.deleteFile(fileName);
//            save(tour);
//        });
    }

    @Override
    public void deleteTourFile(Tour tour, String fileName) throws StorageException {
        String filePath = rootLocation.toString() + PATH_SEPARATOR + tour.getId() + PATH_SEPARATOR + fileName;
        fileSystemStorageService.delete(filePath);
        tour.deleteFile(fileName);
    }

    @Override
    public Resource getTourFile(Long tourId, String fileName) throws StorageException {
        String filePath = rootLocation.toString() + PATH_SEPARATOR + tourId + PATH_SEPARATOR + fileName;
        return fileSystemStorageService.loadAsResource(filePath);
    }

    @Override
    @Transactional
    public void deleteSoft(Long id) {
        tourRepository.softDeleteById(id);
        CACHE.invalidate(id);
    }

    @Override
    @Transactional
    public void deleteSoft(Collection<Long> ids) {
        tourRepository.softDeleteById(ids);
        CACHE.invalidateAll(ids);
    }

    public void deleteAllTourFiles(Tour tour) {
        tour.getFileNames().forEach(fileName -> {
            try {
                deleteTourFile(tour.getId(), fileName);
            } catch (StorageException e) {
                log.warn("unable to delete file {} of tour {} because {}", fileName, tour.getId(), e.getMessage());
            }
        });
    }

}
