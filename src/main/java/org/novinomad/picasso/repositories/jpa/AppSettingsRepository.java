package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.domain.erm.entities.common.AppSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppSettingsRepository extends JpaRepository<AppSettings, String> {
}