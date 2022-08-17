package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.domain.entities.impl.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}
