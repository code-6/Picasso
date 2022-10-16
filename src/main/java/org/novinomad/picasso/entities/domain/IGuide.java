package org.novinomad.picasso.entities.domain;

import org.novinomad.picasso.entities.domain.impl.Guide;

import java.util.Arrays;
import java.util.Collection;

public interface IGuide {

    Collection<String> getLanguages();

    default void addLanguage(String ... languages) {
        getLanguages().addAll(Arrays.asList(languages));
    }

    default void addLanguage(String language) {
        getLanguages().add(language);
    }

    default void addLanguage(Collection<String> languages) {
        getLanguages().addAll(languages);
    }

    default void removeLanguage(String language) {
        getLanguages().removeIf(lang -> lang.equals(language));
    }

    default void removeLanguage(String ... language) {
        getLanguages().removeAll(Arrays.asList(language));
    }

    default void removeLanguage(Collection<String> languages) {
        getLanguages().removeAll(languages);
    }

    default boolean speaks(String language) {
        return getLanguages().contains(language);
    }

    default boolean speaks(String ... language) {
        return getLanguages().containsAll(Arrays.asList(language));
    }

    default boolean speaks(Collection<String> languages) {
        return getLanguages().containsAll(languages);
    }
}
