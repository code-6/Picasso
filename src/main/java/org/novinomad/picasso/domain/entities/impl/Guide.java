package org.novinomad.picasso.domain.entities.impl;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.domain.entities.IGuide;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Guide extends Employee implements IGuide {

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    Set<Language> languages = new HashSet<>();

    //region equals, hashCode, toString
    @Override
    public String toString() {
        return super.toString() + String.format("\tlanguages: %s\n", languages);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Guide guide = (Guide) o;
        return Objects.equals(languages, guide.languages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), languages);
    }
    //endregion

    //region NESTED CLASSES
    /**
     * ISO 639-1
     * */
    @RequiredArgsConstructor
    public enum Language {

        EN("English"),
        DE("German"),
        KY("Kyrgyz"),
        RU("Russian");

        @Getter
        private final String NAME;

        /**
         * Not to be confused with enum constant name.
         * */
        public static Language valueOfName(String name) throws IllegalArgumentException {
            return Arrays.stream(Language.values())
                    .filter(lang -> lang.NAME.equalsIgnoreCase(name.replaceAll("\s", "")))
                    .findFirst().orElseThrow(() -> {
                        String exceptionMessage = String.format("Language constant not present for name: %s", name);
                        return new IllegalArgumentException(exceptionMessage);
                    });
        }
    }

    /**
     * Converts comma separated enum constant names into List of Enum
     * */
    public static class LanguagesToStringConverter implements AttributeConverter<Collection<Language>, String> {

        private static final String delimiter = ",";

        @Override
        public String convertToDatabaseColumn(Collection<Guide.Language> attribute) {
            return attribute.stream()
                    .map(Guide.Language::name)
                    .sorted()
                    .collect(Collectors.joining(delimiter));
        }

        @Override
        public Collection<Guide.Language> convertToEntityAttribute(String dbData) {
            return Arrays.stream(dbData.split(delimiter))
                    .sorted()
                    .map(Guide.Language::valueOf)
                    .collect(Collectors.toList());
        }
    }
    //endregion
}
