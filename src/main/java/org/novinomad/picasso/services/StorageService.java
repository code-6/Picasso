package org.novinomad.picasso.services;

import org.novinomad.picasso.exceptions.StorageException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    boolean existsFolder(String pathToFolder);

    boolean isValidPath(String pathToFolder);

    void store(MultipartFile file, String pathToFolder) throws StorageException, IOException;

    Stream<Path> loadAll(String pathToFolder) throws StorageException;

    Path load(String filename);

    Resource loadAsResource(String filename) throws FileNotFoundException, StorageException;

    void deleteAll(String pathToFolder);
    void delete(String pathToFolder) throws IOException;

    Path createDir(String path) throws IOException, StorageException;

}
