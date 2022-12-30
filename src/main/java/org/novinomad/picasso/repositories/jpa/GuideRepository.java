package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.domain.erm.entities.tour_participants.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuideRepository extends JpaRepository<Guide, Long> {

}
