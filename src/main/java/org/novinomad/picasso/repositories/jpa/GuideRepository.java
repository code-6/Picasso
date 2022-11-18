package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.erm.entities.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuideRepository extends JpaRepository<Guide, Long> {

}
