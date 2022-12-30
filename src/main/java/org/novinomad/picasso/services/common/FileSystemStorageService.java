package org.novinomad.picasso.services.common;

import org.novinomad.picasso.commons.exceptions.StorageException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.Collection;
import java.util.List;

public interface FileSystemStorageService {
    String PATH_SEPARATOR = FileSystems.getDefault().getSeparator();

    default boolean exist(String pathToFolder) {
        try {
            return Files.exists(Paths.get(pathToFolder));
        } catch (InvalidPathException ignored) {
            return false;
        }
    }

    default boolean isValid(String pathToFolder) {
        try {
            Paths.get(pathToFolder);
            return true;
        } catch (InvalidPathException ignored) {
            return false;
        }
    }

    void store(MultipartFile file, String pathToFolder) throws StorageException;

    default void store(String pathToFolder, Collection<MultipartFile> files) throws StorageException {
        for (MultipartFile file : files) {
            if(file != null && !file.isEmpty())
                store(file, pathToFolder);
        }
    }

    List<Path> loadAll(String pathToFolder) throws StorageException;

    Path load(String filename) throws StorageException;

    Resource loadAsResource(String filename) throws StorageException;
    void delete(String pathToFolder) throws StorageException;

    void clearFolder(String pathToFolder, String... exceptionFileNames) throws StorageException;

    Path createFolder(String path) throws StorageException;

}
