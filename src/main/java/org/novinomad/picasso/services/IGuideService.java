package org.novinomad.picasso.services;

import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.erm.entities.Guide;

import java.util.List;

public interface IGuideService extends ICrud<Long, Guide> {

    List<String> getAllLanguages();
}
