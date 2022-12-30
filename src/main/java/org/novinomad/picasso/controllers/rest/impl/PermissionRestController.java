package org.novinomad.picasso.controllers.rest.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.commons.Crud;
import org.novinomad.picasso.commons.enums.GraphSearchAlgorithm;
import org.novinomad.picasso.commons.exceptions.base.CommonException;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.commons.utils.PermissionUtil;
import org.novinomad.picasso.domain.erm.entities.auth.Permission;
import org.novinomad.picasso.services.auth.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionRestController implements Crud<Long, Permission> {

    private final PermissionService permissionService;

    @Override
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public Permission save(Permission permission) {
        return permissionService.save(permission);
    }

    @Override
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        permissionService.deleteById(id);
    }

    @Override
    @GetMapping("/{ids}")
    public List<Permission> getById(@PathVariable Collection<Long> ids) {
        return permissionService.getById(ids);
    }

    @Override
    @GetMapping
    public List<Permission> get() {
        return permissionService.get();
    }

    @Override
    @GetMapping("/{id}")
    public Optional<Permission> getById(@PathVariable Long id) {
        return permissionService.getById(id);
    }

    @PutMapping("/{childPermissionId}/{parentPermissionId}")
    public Permission addChild(@PathVariable Long childPermissionId, @PathVariable Long parentPermissionId) {

        Permission parentPermission = permissionService.getOrElseThrow(parentPermissionId);

        Permission childPermission = permissionService.getOrElseThrow(childPermissionId);

        if(parentPermission.hasChild(childPermission)) {
            throw new IllegalStateException(String.format("permission {%s} already has {%s} as child. No sense in inheritance", parentPermission.getName(), childPermission.getName()));
        }
        parentPermission.addChild(childPermission);

        if(parentPermission.hasCycle()) {
            parentPermission.deleteChild(childPermission);
            throw new IllegalStateException(String.format("permission {%s} can not has {%s} as child. Directed acyclic graph should not have cycle", parentPermission.getName(), childPermission.getName()));
        }
        parentPermission.addChild(childPermission);

        return save(parentPermission);
    }

    @DeleteMapping("/{childPermissionId}/{parentPermissionId}")
    public void deleteChild(@PathVariable Long childPermissionId, @PathVariable Long parentPermissionId) {

        Permission parentPermission = permissionService.getOrElseThrow(parentPermissionId);

        parentPermission.deleteChild(childPermissionId);

        permissionService.save(parentPermission);
    }

    @GetMapping(value = {"/traverse/{permissionId}", "/traverse/{permissionId}/{algorithm}"})
    public LinkedHashMap<Long,String> traverse(@PathVariable Long permissionId, @PathVariable(required = false) GraphSearchAlgorithm algorithm) {
        if(algorithm != null && algorithm.equals(GraphSearchAlgorithm.DFS)) {
            return PermissionUtil.traverseDFS(permissionService.getOrElseThrow(permissionId));
        } else {
            return PermissionUtil.traverseBFS(permissionService.getOrElseThrow(permissionId));
        }
    }

//    @GetMapping("/paths/{permissionId}")
//    public List<LinkedHashSet<Pair<Long,String>>>  getPathsLeadingTo(@PathVariable("permissionId") Long permissionId) {
//        Permission permission = permissionService.getOrElseThrow(permissionId);
//        return PermissionService.getPathsLeadingTo(permission);
//    }
//
//    @GetMapping(value = {"/search-paths/{permissionId}", "/traverse/{permissionId}/{algorithm}"})
//    public List<LinkedHashSet<Pair<Long,String>>> search(@PathVariable Long permissionId, @PathVariable(required = false) GraphSearchAlgorithm algorithm) {
//        if(algorithm != null && algorithm.equals(GraphSearchAlgorithm.DFS)) {
//        } else {
//        }
//    }
}
