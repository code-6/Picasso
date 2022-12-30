package org.novinomad.picasso.services.auth;

import org.novinomad.picasso.commons.Crud;
import org.novinomad.picasso.domain.dto.permissions.VisJsDataSet;
import org.novinomad.picasso.domain.erm.entities.auth.Permission;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface PermissionService extends Crud<Long, Permission> {
    String ROOT_PERMISSION_NAME = "full access";

    Optional<Permission> get(String permissionName);

    VisJsDataSet getAllForVisJs();

    default Permission getOrElseThrow(Long id) {
        return getById(id)
                .orElseThrow(() -> new NoSuchElementException("permission with id " + id + " doesn't exists" ));
    }

}
