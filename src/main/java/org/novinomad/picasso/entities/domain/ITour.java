package org.novinomad.picasso.entities.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ITour {
    Set<String> getFileNames();

    default void addFileName(String ... files) {
        getFileNames().addAll(Arrays.asList(files));
    }

    default void addFileName(String file) {
        getFileNames().add(file);
    }

    default void addFileName(Collection<String> files) {
        getFileNames().addAll(files);
    }
    default void addFile(Collection<MultipartFile> files) {
        files.forEach(f -> {
            String originalFilename = f.getOriginalFilename();
            if(StringUtils.isNotBlank(originalFilename))
                addFileName(originalFilename);
        });
    }

    default void removeFile(String file) {
        getFileNames().removeIf(lang -> lang.equals(file));
    }

    default void removeFile(String ... file) {
        getFileNames().removeAll(Arrays.asList(file));
    }

    default void removeFile(Collection<String> files) {
        getFileNames().removeAll(files);
    }
}
