package org.novinomad.picasso.controllers.restapi.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.commons.enums.GraphSearchAlgorithm;
import org.novinomad.picasso.commons.exceptions.base.CommonException;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.erm.entities.system.Permission;
import org.novinomad.picasso.services.IPermissionService;
import org.novinomad.picasso.services.impl.PermissionService;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
@Slf4j
public class PermissionRestController implements ICrud<Long, Permission> {

    private final IPermissionService permissionService;

    @PostMapping
    @Override
    public Permission save(Permission permission) {

        try {
            Permission savedPermission = permissionService.save(permission);
            log.info("saved permission {}", permission);
            return savedPermission;
        } catch (Exception e) {
            log.error("unable to save permission {} because {}", permission, e.getMessage());
            throw new CommonRuntimeException("unable to save permission {}", permission);
        }
    }

    @DeleteMapping("/{permissionId}")
    @Override
    public void deleteById(@PathVariable Long permissionId) {
        permissionService.deleteById(permissionId);
    }

    @GetMapping(value = {"","/{permissionIds}"})
    @Override
    public List<Permission> get(@PathVariable(required = false) Long... permissionIds) {
        return permissionService.get(permissionIds);
    }

    @Override
    public Optional<Permission> get(Long permissionId) {
        return permissionService.get(permissionId);
    }

    @GetMapping("/{permissionId}")
    public Permission getPermission(@PathVariable Long permissionId) {
        return get(permissionId).orElseThrow(() -> new NoSuchElementException("permission with name " + permissionId + " does not exists"));
    }

    @PutMapping("/{childPermissionId}/{parentPermissionId}")
    public Permission addChild(@PathVariable Long childPermissionId, @PathVariable Long parentPermissionId) throws CommonException {

        Permission parentPermission = permissionService.getOrElseThrow(parentPermissionId);

        Permission childPermission = permissionService.getOrElseThrow(childPermissionId);

        return save(parentPermission.addChild(childPermission));
    }

    @DeleteMapping("/{childPermissionId}/{parentPermissionId}")
    public ResponseEntity<String> deleteChild(@PathVariable Long childPermissionId, @PathVariable Long parentPermissionId) {

        try {
            Permission parentPermission = permissionService.getOrElseThrow(parentPermissionId);
            permissionService.save(parentPermission.deleteChild(childPermissionId));
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            log.error("unable to delete child permission {} of parent permission {} because {}", childPermissionId, parentPermissionId, e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("unable to delete child permission {} of parent permission {} because {}", childPermissionId, parentPermissionId, e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/traverse/{permissionId}", "/traverse/{permissionId}/{algorithm}"})
    public LinkedHashMap<Long,String> traverse(@PathVariable Long permissionId, @PathVariable(required = false) GraphSearchAlgorithm algorithm) {
        if(algorithm != null && algorithm.equals(GraphSearchAlgorithm.DFS)) {
            return PermissionService.traverseDFS(permissionService.getOrElseThrow(permissionId));
        } else {
            return PermissionService.traverseBFS(permissionService.getOrElseThrow(permissionId));
        }
    }

//    @GetMapping("/paths/{permissionId}")
//    public List<LinkedHashSet<Pair<Long,String>>>  getPathsLeadingTo(@PathVariable("permissionId") Long permissionId) {
//        Permission permission = permissionService.getOrElseThrow(permissionId);
//        return PermissionService.getPathsLeadingTo(permission);
//    }

//    @GetMapping(value = {"/search-paths/{permissionId}", "/traverse/{permissionId}/{algorithm}"})
//    public List<LinkedHashSet<Pair<Long,String>>> search(@PathVariable Long permissionId, @PathVariable(required = false) GraphSearchAlgorithm algorithm) {
//        if(algorithm != null && algorithm.equals(GraphSearchAlgorithm.DFS)) {
//        } else {
//        }
//    }
}
