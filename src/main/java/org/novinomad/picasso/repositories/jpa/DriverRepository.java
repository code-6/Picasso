package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.entities.domain.impl.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}
