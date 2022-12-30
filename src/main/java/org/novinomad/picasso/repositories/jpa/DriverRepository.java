package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.domain.erm.entities.tour_participants.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}
