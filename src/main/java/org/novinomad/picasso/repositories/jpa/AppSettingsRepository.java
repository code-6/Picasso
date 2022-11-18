package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.erm.entities.system.AppSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppSettingsRepository extends JpaRepository<AppSettings, String> {
}