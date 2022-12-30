package org.novinomad.picasso.domain.dto.tour_participants;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.domain.dto.AbstractDto;
import org.novinomad.picasso.domain.IGuide;
import org.novinomad.picasso.domain.erm.entities.tour_participants.Guide;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;

import java.util.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GuideDto extends AbstractDto implements IGuide {

    String name;

    TourParticipant.Type type;

    @Builder.Default
    List<String> languages = new LinkedList<>();
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public List<String> getLanguages() {
        return languages;
    }

    @Override
    public Guide toEntity() {
        Guide guide = new Guide();

        guide.setId(id);
        guide.setName(name);
        guide.setType(type);
        guide.setLanguages(languages);

        return guide;
    }
}
