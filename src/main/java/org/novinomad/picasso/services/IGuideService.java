package org.novinomad.picasso.services;

import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.entities.domain.impl.Guide;

import java.util.List;

public interface IGuideService extends ICrud<Guide> {

    List<String> getAllLanguages();
}
