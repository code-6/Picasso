package org.novinomad.picasso.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.novinomad.picasso.exceptions.StorageException;
import org.novinomad.picasso.services.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
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
    public boolean existsFolder(String pathToFolder) {
        boolean exists = false;
        try {
            exists = Files.exists(Paths.get(pathToFolder));
        } catch (InvalidPathException e) {
            log.error(e.getMessage(), e);
        }
        return exists;
    }

    @Override
    public boolean isValidPath(String pathToFolder) {
        boolean isValid = false;
        try {
            Paths.get(pathToFolder);
            isValid = true;
        } catch (InvalidPathException e) {
            log.error(e.getMessage(), e);
        }
        return isValid;
    }

    @Override
    public void store(MultipartFile file, String pathToFolder) throws IOException, StorageException {
        try {
            if (file != null && !file.isEmpty()) {
                if (isValidPath(pathToFolder)) {
                    Path path;
                    if (!existsFolder(pathToFolder)) path = createDir(pathToFolder);
                    else path = Paths.get(pathToFolder);
                    String originalFilename = file.getOriginalFilename();

                    if (StringUtils.isNotBlank(originalFilename)) {
                        Path destinationFile = path.resolve(
                                        Paths.get(originalFilename))
                                .normalize().toAbsolutePath();

                        try (InputStream inputStream = file.getInputStream()) {
                            Files.copy(inputStream, destinationFile,
                                    StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                }
            }
        } catch (IOException | StorageException e) {
            log.error("failed to store file {} because {}", file, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Stream<Path> loadAll(String pathToFolder) throws StorageException {
        if (isValidPath(pathToFolder) && existsFolder(pathToFolder)) {
            Path destinationPath = Paths.get(pathToFolder);
            try (Stream<Path> stream = Files.walk(destinationPath, 1)) {
                return stream.filter(path -> !path.equals(destinationPath))
                        .map(destinationPath::relativize);
            } catch (IOException e) {
                throw new StorageException("Failed to read stored files", e);
            }
        }
        return null;
    }

    @Override
    public Path load(String filename) {
        return Paths.get(filename);
    }

    @Override
    public Resource loadAsResource(String filename) throws StorageException {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException(
                        "Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageException("Could not read file: " + filename, e);
        }
    }

    public List<MultipartFile> loadAsMultipart(String folder) throws StorageException {
        return null;
    }

    @Override
    public void deleteAll(String pathToFolder) {

    }

    @Override
    public void delete(String pathToFolder) throws IOException {
        Files.delete(Paths.get(pathToFolder));
    }

    @Override
    public Path createDir(String path) throws IOException, StorageException {
        if (isValidPath(path))
            return Files.createDirectory(Paths.get(path));

        throw new StorageException("unable to create new directory {}", path);
    }
}
