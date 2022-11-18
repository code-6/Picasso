package org.novinomad.picasso.services.impl;

import lombok.RequiredArgsConstructor;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.erm.entities.system.GuiAccessConfig;
import org.novinomad.picasso.repositories.jpa.UiComponentPermissionsRepository;
import org.novinomad.picasso.services.IUiPermissionService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UiPermissionService implements IUiPermissionService {
    
    final UiComponentPermissionsRepository uiComponentPermissionsRepository;

    @Override
    public GuiAccessConfig save(GuiAccessConfig permission) {
        try {
            return uiComponentPermissionsRepository.save(permission);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to create UI permission {} because {}", permission, e.getMessage());
        }
    }

    @Override
    public void deleteById(String permissionName) {
        try {
            uiComponentPermissionsRepository.deleteById(permissionName);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to delete UI permission {} because {}", permissionName, e.getMessage());
        }
    }

    @Override
    public List<GuiAccessConfig> get(String... permissionNames) {
        try {
            if(permissionNames == null || permissionNames.length == 0) {
                return uiComponentPermissionsRepository.findAll();
            } else {
                return uiComponentPermissionsRepository.findAllById(Arrays.asList(permissionNames));
            }
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to get UI permissions {} because {}",permissionNames, e.getMessage());
        }
    }

    @Override
    public Optional<GuiAccessConfig> get(String permissionName) {
        try {
            return uiComponentPermissionsRepository.findById(permissionName);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to get UI permission {} because {}", permissionName, e.getMessage());
        }
    }
}
