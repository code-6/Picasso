package org.novinomad.picasso.domain.entities;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public interface ITour {
    Set<String> getFiles();

    default void addFile(String ... files) {
        getFiles().addAll(Arrays.asList(files));
    }

    default void addFile(String file) {
        getFiles().add(file);
    }

    default void addFile(Collection<String> files) {
        getFiles().addAll(files);
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
