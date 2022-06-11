package org.novinomad.picasso.repositories;

import org.novinomad.picasso.domain.entities.impl.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Tour, Long> {
}
