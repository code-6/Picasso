package org.novinomad.picasso.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.entities.base.EntityConvertable;
import org.novinomad.picasso.entities.domain.IGuide;
import org.novinomad.picasso.entities.domain.impl.Guide;
import org.novinomad.picasso.entities.domain.impl.TourParticipant;
import org.novinomad.picasso.dto.base.AbstractModel;

import java.util.*;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GuideModel extends AbstractModel implements IGuide {

    String name;

    TourParticipant.Type type;

    List<String> languages = new ArrayList<>();
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Collection<String> getLanguages() {
        return languages;
    }

    @Override
    public Guide toEntity() {
        Guide guide = new Guide();

        guide.setId(id);
        guide.setName(name);
        guide.setType(type);
        guide.setLanguages(new HashSet<>(languages));

        return guide;
    }
}
