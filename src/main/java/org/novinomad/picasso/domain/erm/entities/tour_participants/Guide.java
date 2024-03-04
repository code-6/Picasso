package org.novinomad.picasso.domain.erm.entities.tour_participants;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.novinomad.picasso.commons.collections.CommonCollections;
import org.novinomad.picasso.domain.dto.tour_participants.GuideDto;
import org.novinomad.picasso.domain.IGuide;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Guide extends TourParticipant implements IGuide {

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE) // fix org.hibernate.loader.MultipleBagFetchException and lazy initialization exception
    @CollectionTable(indexes = @Index(columnList = "languages"))
    List<String> languages = new LinkedList<>();

    public Guide(String name) {
        super(name, Type.GUIDE);
    }

    public Guide(Long id, String name) {
        this(name);
        this.id = id;
    }

    @Override
    public GuideDto toModel() {
        GuideDto guideModel = new GuideDto();
        guideModel.setId(id);
        guideModel.setType(type);
        guideModel.setName(name);
        guideModel.setLanguages(new ArrayList<>(languages));
        return guideModel;
    }

    //region equals, hashCode, toString
    @Override
    public String toString() {
        return super.toString().replace("}", "") +
                ", languages=" + languages +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Guide guide = (Guide) o;
        return Objects.equals(languages, guide.languages);
    }

    public String getLanguagesAsString() {
        return CommonCollections.toString("; ", "", "", languages);
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

        public static String random() {
            Language[] languages = values();
            int langIndex = RandomUtils.nextInt(0, languages.length - 1);
            return languages[langIndex].NAME;
        }
        public static Set<String> randomSet() {
            return randomSet(1, 0);
        }

        public static Set<String> randomSet(int minCount, int maxCount) {

            maxCount = Math.abs(maxCount);

            int maxAvailableLangCount = values().length;

            if(minCount < 1 || minCount > maxCount) minCount = 1;

            if(maxCount > maxAvailableLangCount || maxCount < minCount) maxCount = maxAvailableLangCount;

            int langCountToReturn = RandomUtils.nextInt(minCount, maxCount);

            Set<String> languages = new HashSet<>();

            while(languages.size() < langCountToReturn) languages.add(Language.random());

            return languages;
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