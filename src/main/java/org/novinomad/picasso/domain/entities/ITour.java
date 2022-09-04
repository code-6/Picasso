package org.novinomad.picasso.domain.entities;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public interface ITour {
    Set<String> getFiles();

    default void addFileName(String ... files) {
        getFiles().addAll(Arrays.asList(files));
    }

    default void addFileName(String file) {
        getFiles().add(file);
    }

    default void addFileName(Collection<String> files) {
        getFiles().addAll(files);
    }
    default void addFile(Collection<MultipartFile> files) {
        files.forEach(f -> {
            String originalFilename = f.getOriginalFilename();
            if(StringUtils.isNotBlank(originalFilename))
                addFileName(originalFilename);
        });
    }

    default void removeFile(String file) {
        getFiles().removeIf(lang -> lang.equals(file));
    }

    default void removeFile(String ... file) {
        getFiles().removeAll(Arrays.asList(file));
    }

    default void removeFile(Collection<String> files) {
        getFiles().removeAll(files);
    }
}
