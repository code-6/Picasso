package org.novinomad.picasso.services.impl;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.erm.dto.VisJsDataSet;
import org.novinomad.picasso.erm.entities.system.Permission;
import org.novinomad.picasso.erm.entities.system.User;
import org.novinomad.picasso.repositories.jpa.PermissionRepository;
import org.novinomad.picasso.services.IPermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PermissionService implements IPermissionService {

    final PermissionRepository permissionRepository;

    @PostConstruct
    void buildBasePermissionGraph() {

        List<Permission> all = permissionRepository.findAll();

        if(all.isEmpty()) {
            Permission permissionGraph = new Permission("full access", "allowed to do anything in the system")
                    .addChild(
                            new Permission("read", "allowed to see anything in the system")
                                    .addChild("read tours", "allowed to see list of tours")
                                    .addChild("read tour participants", "allowed to see list of tour participants of any type")
                                    .addChild("read tour binds", "allowed to see list of tour binds (on main page of the system)")
                                    .addChild("read users", "allowed to see list of users")
                                    .addChild("read permissions", "allowed to see permissions graph")
                    )
                    .addChild(
                            new Permission("create", "allowed to create anything in the system")
                                    .addChild("create tours", "allowed to create tours")
                                    .addChild("create tour participants", "allowed to create tour participants of any type")
                                    .addChild("create tour binds", "allowed to create tour binds (on main page of the system)")
                                    .addChild("create users", "allowed to create users")
                                    .addChild("create permissions", "allowed to create permissions")
                    )
                    .addChild(
                            new Permission("update", "allowed to update anything in the system")
                                    .addChild("update tours", "allowed to update tours")
                                    .addChild("update tour participants", "allowed to update tour participants of any type")
                                    .addChild("update tour binds", "allowed to update tour binds (on main page of the system)")
                                    .addChild("update users", "allowed to update users")
                                    .addChild("update permissions", "allowed to update permissions")
                    )
                    .addChild(
                            new Permission("delete", "allowed to delete anything in the system")
                                    .addChild("delete tours", "allowed to delete tours")
                                    .addChild("delete tour participants", "allowed to delete tour participants of any type")
                                    .addChild("delete tour binds", "allowed to delete tour binds (on main page of the system)")
                                    .addChild("delete users", "allowed to delete users")
                                    .addChild("delete permissions", "allowed to delete permissions")
                    );
            permissionRepository.save(permissionGraph);
        }
    }

    @Override
    public Permission save(Permission permission) {
        try {
            return permissionRepository.save(permission);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to create permission {} because {}", permission, e.getMessage());
        }
    }

    @Override
    public void deleteById(Long permissionId) {
        try {
            permissionRepository.deleteById(permissionId);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to delete permission {} because {}", permissionId, e.getMessage());
        }
    }

    @Override
    public List<Permission> get(Long... permissionIds) {
        try {
            if (permissionIds == null || permissionIds.length == 0) {
                return permissionRepository.findAll();
            } else {
                return permissionRepository.findAllById(Arrays.asList(permissionIds));
            }
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to get permissions {} because {}", permissionIds, e.getMessage());
        }
    }

    @Override
    public Optional<Permission> get(Long permissionId) {
        try {
            return permissionRepository.findById(permissionId);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to get permission {} because {}", permissionId, e.getMessage());
        }
    }

    @Override
    public Optional<Permission> get(String permissionName) {
        try {
            return permissionRepository.findByName(permissionName);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to get permission {} because {}", permissionName, e.getMessage());
        }
    }

    @Override
    public VisJsDataSet asVisJsDataSet() {

        List<Permission> rootPermissions = permissionRepository.getRootPermissions();

        VisJsDataSet visJsDataSet = new VisJsDataSet();

        for (Permission rootPermission : rootPermissions) {
            asVisJsDataSet(rootPermission, visJsDataSet);
        }
        return visJsDataSet;
    }

    private void asVisJsDataSet(Permission permission, final VisJsDataSet visJsDataSet) {

        VisJsDataSet.Node parentNode = permission.toVisJsNode();

        visJsDataSet.addNode(parentNode);

        for (Permission childPermission : permission.getChildren()) {
            VisJsDataSet.Node childNode = childPermission.toVisJsNode();
            visJsDataSet.addEdge(childNode, parentNode);
            asVisJsDataSet(childPermission, visJsDataSet);
        }
    }

    public static Set<Permission> getRoots(@NotNull Permission permission, @NotNull Set<Permission> rootPermissions) {

        Set<Permission> parents = permission.getClosestParents();

        if (parents.isEmpty()) {
            rootPermissions.add(permission);
        } else {
            for (Permission parent : parents) {
                PermissionService.getRoots(parent, rootPermissions);
            }
        }
        return rootPermissions;
    }

    /**
     * Traverse using Breadth First Search from provided permission until last lower permission.
     *
     * @param permission permission where from to start traverse.
     * @return visited child permissions including initial permission as map where key is permission id and value is permission name.
     */
    public static LinkedHashMap<Long, String> traverseBFS(Permission permission) {
        Queue<Permission> permissionQueue = new LinkedList<>();
        LinkedHashMap<Long, String> visitedPermissionIds = new LinkedHashMap<>();

        permissionQueue.add(permission);
        visitedPermissionIds.put(permission.getId(), permission.getName());

        while (!permissionQueue.isEmpty()) {
            Permission currentPermission = permissionQueue.remove();

            for (Permission childPermission : currentPermission.getChildren()) { // Check each neighbor node
                if (!visitedPermissionIds.containsKey(childPermission.getId())) { // If neighbor node's value is not in visited set
                    permissionQueue.add(childPermission);
                    visitedPermissionIds.put(childPermission.getId(), childPermission.getName());
                }
            }
        }
        return visitedPermissionIds;
    }

    /**
     * Traverse using Deep First Search from provided permission until last lower permission.
     *
     * @param permission permission where from to start traverse.
     * @return visited child permissions including initial permission as map where key is permission id and value is permission name.
     */
    public static LinkedHashMap<Long, String> traverseDFS(Permission permission) {
        Stack<Permission> permissionStack = new Stack<>();

        LinkedHashMap<Long, String> visitedPermissionIds = new LinkedHashMap<>();

        permissionStack.push(permission);

        while (!permissionStack.isEmpty()) {
            Permission currentPermission = permissionStack.pop();

            if (!visitedPermissionIds.containsKey(currentPermission.getId())) { // if current node has not been visited yet
                visitedPermissionIds.put(currentPermission.getId(), currentPermission.getName());
            }

            for (Permission childPermission : currentPermission.getChildren()) { // check each neighbor node. Push all unvisited nodes into stack
                if (!visitedPermissionIds.containsKey(childPermission.getId())) {
                    permissionStack.push(childPermission);
                }
            }
        }
        return visitedPermissionIds;
    }

    public boolean authorize(User user, Set<Permission> resourcePermissions) {
        Set<Permission> userPermissions = user.getPermissions();

        if(userPermissions.containsAll(resourcePermissions))
            return true;

        for (Permission userPermission : userPermissions) {
            if(userPermission.containsAll(resourcePermissions))
                return true;
        }
        return false;
    }

    public static List<LinkedHashSet<String>> getPathsLeadingTo(Permission permission) {

        throw new NotImplementedException();
    }

    public static List<LinkedHashSet<String>> getPathsStartingFrom(Permission permission) {

        throw new NotImplementedException();
    }

    public static boolean hasCycle(Permission permission) {
        permission.setBeingVisited(true);

        for (Permission childPermission : permission.getChildren()) {
            if (childPermission.isBeingVisited()) {
                // backward edge exists
                return true;
            } else if (!childPermission.isVisited() && hasCycle(childPermission)) {
                return true;
            }
        }

        permission.setBeingVisited(false);
        permission.setVisited(true);
        return false;
    }
}
