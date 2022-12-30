package org.novinomad.picasso.commons.utils;

import com.sun.istack.NotNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.NotImplementedException;
import org.novinomad.picasso.domain.dto.permissions.VisJsDataSet;
import org.novinomad.picasso.domain.erm.entities.auth.IPermission;
import org.novinomad.picasso.domain.erm.entities.auth.Permission;
import org.novinomad.picasso.domain.erm.entities.auth.User;

import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class PermissionUtil {

    public static VisJsDataSet toVisJsDataSet(Collection<? extends IPermission> permissions) {
        VisJsDataSet visJsDataSet = new VisJsDataSet();

        for (IPermission permission : permissions) {
            PermissionUtil.toVisJsDataSet(permission, visJsDataSet);
        }
        return visJsDataSet;
    }

    private static void toVisJsDataSet(IPermission permission, final VisJsDataSet visJsDataSet) {

        VisJsDataSet.Node parentNode = permission.toVisJsNode();

        visJsDataSet.addNode(parentNode);

        for (IPermission childPermission : permission.getChildren()) {
            VisJsDataSet.Node childNode = childPermission.toVisJsNode();
            visJsDataSet.addEdge(childNode, parentNode);
            toVisJsDataSet(childPermission, visJsDataSet);
        }
    }

    public static Set<IPermission> getRoots(@NotNull IPermission permission, @NotNull Set<IPermission> rootPermissions) {

        Set<IPermission> parents = permission.getClosestParents();

        if (parents.isEmpty()) {
            rootPermissions.add(permission);
        } else {
            for (IPermission parent : parents) {
                getRoots(parent, rootPermissions);
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
    public static LinkedHashMap<Long, String> traverseBFS(IPermission permission) {
        Queue<IPermission> permissionQueue = new LinkedList<>();
        LinkedHashMap<Long, String> visitedPermissionIds = new LinkedHashMap<>();

        permissionQueue.add(permission);
        visitedPermissionIds.put(permission.getId(), permission.getName());

        while (!permissionQueue.isEmpty()) {
            IPermission currentPermission = permissionQueue.remove();

            for (IPermission childPermission : currentPermission.getChildren()) { // Check each neighbor node
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
    public static LinkedHashMap<Long, String> traverseDFS(IPermission permission) {
        Stack<IPermission> permissionStack = new Stack<>();

        LinkedHashMap<Long, String> visitedPermissionIds = new LinkedHashMap<>();

        permissionStack.push(permission);

        while (!permissionStack.isEmpty()) {
            IPermission currentPermission = permissionStack.pop();

            if (!visitedPermissionIds.containsKey(currentPermission.getId())) { // if current node has not been visited yet
                visitedPermissionIds.put(currentPermission.getId(), currentPermission.getName());
            }

            for (IPermission childPermission : currentPermission.getChildren()) { // check each neighbor node. Push all unvisited nodes into stack
                if (!visitedPermissionIds.containsKey(childPermission.getId())) {
                    permissionStack.push(childPermission);
                }
            }
        }
        return visitedPermissionIds;
    }

    public boolean accessAllowed(Set<IPermission> userPermissions, Set<IPermission> resourcePermissions) {
        if(userPermissions.containsAll(resourcePermissions))
            return true;

        for (IPermission userPermission : userPermissions) {
            if(userPermission.containsAll(resourcePermissions))
                return true;
        }
        return false;
    }

    public static List<LinkedHashSet<String>> getPathsLeadingTo(IPermission permission) {

        throw new NotImplementedException();
    }

    public static List<LinkedHashSet<String>> getPathsStartingFrom(IPermission permission) {

        throw new NotImplementedException();
    }

    public static boolean hasCycle(IPermission permission, Map<String, Boolean> beingVisitedPermissions, Map<String, Boolean> visitedPermissions) {

        if(beingVisitedPermissions == null) {
            beingVisitedPermissions = new HashMap<>();
        }
        if(visitedPermissions == null) {
            visitedPermissions = new HashMap<>();
        }
        beingVisitedPermissions.put(permission.getName(), true);

        for (IPermission childPermission : permission.getChildren()) {
            boolean isChildPermissionBeingVisited = beingVisitedPermissions.get(childPermission.getName()) != null
                    ? beingVisitedPermissions.get(childPermission.getName()) : false;

            boolean isChildPermissionVisited = visitedPermissions.get(childPermission.getName()) != null
                    ? visitedPermissions.get(childPermission.getName()) : false;

            if ( isChildPermissionBeingVisited) {
                // backward edge exists
                return true;
            } else if (!isChildPermissionVisited && hasCycle(childPermission, beingVisitedPermissions, visitedPermissions)) {
                return true;
            }
        }
        beingVisitedPermissions.put(permission.getName(), false);
        visitedPermissions.put(permission.getName(), true);
        return false;
    }

    public static boolean hasChild(IPermission parent, IPermission child) {
        if(parent == null || child == null) {
            throw new IllegalArgumentException("parent or child may not be null");
        }
        LinkedHashMap<Long, String> traversedPermissions = PermissionUtil.traverseBFS(parent);

        return traversedPermissions.containsValue(child.getName())
                || (child.getId() != null && traversedPermissions.containsKey(child.getId()));
    }

}
