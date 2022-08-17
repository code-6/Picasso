package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.domain.entities.impl.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuideRepository extends JpaRepository<Guide, Long> {

}
