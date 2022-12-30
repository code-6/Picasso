package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.domain.erm.entities.auth.GuiAccessConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UiComponentPermissionsRepository extends JpaRepository<GuiAccessConfig, String> {
}