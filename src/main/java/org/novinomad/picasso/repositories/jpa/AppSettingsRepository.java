package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.entities.AppSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppSettingsRepository extends JpaRepository<AppSettings, String> {
}