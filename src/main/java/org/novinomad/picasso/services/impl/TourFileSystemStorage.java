package org.novinomad.picasso.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.commons.exceptions.StorageException;
import org.novinomad.picasso.services.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class TourFileSystemStorage implements StorageService {

    @Override
    public void store(MultipartFile file, String pathToFolder) throws StorageException {

        String originalFilename = null;
        try {
            if(file == null || file.isEmpty())
                throw new IllegalArgumentException("file is null or empty");

            Path path;

            if (!exist(pathToFolder)) path = createFolder(pathToFolder);
            else path = Paths.get(pathToFolder);

            originalFilename = file.getOriginalFilename();

            if(originalFilename == null)
                throw new IllegalStateException("original file name is null");

            Path destinationFile = path.resolve(Paths.get(originalFilename)).normalize().toAbsolutePath();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException | IllegalStateException | IllegalArgumentException e) {
            StorageException storageException = new StorageException("unable to store file {} in folder {} because {}",
                    originalFilename, pathToFolder, e.getMessage(), e);
            log.error(storageException.getMessage(), e);
            throw storageException;
        }
    }

    @Override
    public List<Path> loadAll(String pathToFolder) throws StorageException {
        try {
            Path destinationPath = Paths.get(pathToFolder);

            try (Stream<Path> stream = Files.walk(destinationPath, 1)) {
                return stream.filter(path -> !path.equals(destinationPath)).map(destinationPath::relativize).toList();
            }
        } catch (Exception e) {
            StorageException storageException = new StorageException("unable to load files in folder {} because {}", pathToFolder, e.getMessage(), e);
            log.error(storageException.getMessage(), e);
            throw storageException;
        }
    }

    @Override
    public Path load(String path) throws StorageException {
        try {
            return Paths.get(path);
        } catch (InvalidPathException e) {
            StorageException storageException = new StorageException("unable to load file {} because {}", path, e.getMessage(), e);
            log.error(storageException.getMessage(), e);
            throw storageException;
        }
    }

    @Override
    public Resource loadAsResource(String filename) throws StorageException {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new IllegalStateException("resource doesn't exists or is not readable");
            }
        } catch (MalformedURLException | IllegalStateException e) {
            StorageException storageException = new StorageException("unable to read file {} because {} ", filename, e.getMessage(), e);
            log.error(storageException.getMessage());
            throw storageException;
        }
    }

    @Override
    public void delete(String path) throws StorageException {
        try {
            Files.delete(Paths.get(path));
        } catch (Exception e) {
            StorageException storageException = new StorageException("unable to delete {} because {}", path, e.getMessage(), e);
            log.error(storageException.getMessage(), e);
            throw storageException;
        }
    }

    @Override
    public void clearFolder(String pathToFolder, String... exceptFileNames) throws StorageException {
        loadAll(pathToFolder).forEach(path -> {
            String fileName = path.getFileName().toString();
            String fileNameToCompare = fileName.toLowerCase().replaceAll("\s", "");
            for (String exceptFileName : exceptFileNames) {
                if(!fileNameToCompare.contains(exceptFileName.toLowerCase().replaceAll("\s", ""))) {
                    try {
                        delete(pathToFolder + PATH_SEPARATOR + fileName);
                    } catch (StorageException ignored) {}
                }
            }
        });
    }

    @Override
    public Path createFolder(String path) throws StorageException {
        try {
            return Files.createDirectory(Paths.get(path));
        } catch (IOException e) {
            StorageException storageException = new StorageException("unable to create folder {} because {}", path, e.getMessage(), e);
            log.error(storageException.getMessage(), e);
            throw storageException;
        }
    }
}
