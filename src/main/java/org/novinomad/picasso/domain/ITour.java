package org.novinomad.picasso.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

public interface ITour {
    List<String> getFileNames();

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
        for (MultipartFile f : files) {
            if(f != null && !f.isEmpty()) {
                String originalFilename = f.getOriginalFilename();
                if (StringUtils.isNotBlank(originalFilename))
                    addFileName(originalFilename);
            }
        }
    }

    default void deleteFile(String file) {
        Iterator<String> iterator = getFileNames().iterator();
        while (iterator.hasNext()) {
            if(StringUtils.isNotBlank(file) && file.equals(iterator.next())) {
                iterator.remove();
            }
        }
    }

    default void deleteFile(String ... file) {
        List<String> fileNames = getFileNames();
        Iterator<String> iterator = fileNames.iterator();
        while (iterator.hasNext()) {
            for (String f : file) {
                if(StringUtils.isNotBlank(f) && f.equals(iterator.next())) {
                    iterator.remove();
                }
            }
        }
    }

    default void deleteFile(Collection<String> files) {
        Iterator<String> iterator = getFileNames().iterator();
        while (iterator.hasNext()) {
            if(files.contains(iterator.next())) {
                iterator.remove();
            }
        }
    }
}
