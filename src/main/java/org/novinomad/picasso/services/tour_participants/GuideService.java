package org.novinomad.picasso.services.tour_participants;

import org.novinomad.picasso.commons.Crud;
import org.novinomad.picasso.domain.erm.entities.tour_participants.Guide;

import java.util.List;

public interface GuideService extends Crud<Long, Guide> {
    List<String> getLanguages();
}
