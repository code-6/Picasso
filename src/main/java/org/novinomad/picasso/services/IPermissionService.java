package org.novinomad.picasso.services;

import com.sun.istack.NotNull;
import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.commons.exceptions.base.CommonException;
import org.novinomad.picasso.erm.dto.VisJsDataSet;
import org.novinomad.picasso.erm.entities.system.Permission;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

public interface IPermissionService extends ICrud<Long, Permission> {

    Optional<Permission> get(String permissionName);

    VisJsDataSet asVisJsDataSet();

    default Permission getOrElseThrow(Long id) {
        return get(id).orElseThrow(() -> new NoSuchElementException("permission with id " + id + " doesn't exists" ));
    }

}
