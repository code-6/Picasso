package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.erm.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}
