package org.novinomad.picasso.entities.domain;

import org.novinomad.picasso.entities.domain.impl.Guide;

import java.util.Arrays;
import java.util.Collection;

public interface IGuide {

    Collection<Guide.Language> getLanguages();

    default void addLanguage(Guide.Language ... languages) {
        getLanguages().addAll(Arrays.asList(languages));
    }

    default void addLanguage(Guide.Language language) {
        getLanguages().add(language);
    }

    default void addLanguage(Collection<Guide.Language> languages) {
        getLanguages().addAll(languages);
    }

    default void removeLanguage(Guide.Language language) {
        getLanguages().removeIf(lang -> lang.equals(language));
    }

    default void removeLanguage(Guide.Language ... language) {
        getLanguages().removeAll(Arrays.asList(language));
    }

    default void removeLanguage(Collection<Guide.Language> languages) {
        getLanguages().removeAll(languages);
    }

    default boolean speaks(Guide.Language language) {
        return getLanguages().contains(language);
    }

    default boolean speaks(Guide.Language ... language) {
        return getLanguages().containsAll(Arrays.asList(language));
    }

    default boolean speaks(Collection<Guide.Language> languages) {
        return getLanguages().containsAll(languages);
    }
}
