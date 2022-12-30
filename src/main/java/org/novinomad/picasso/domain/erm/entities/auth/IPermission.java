package org.novinomad.picasso.domain.erm.entities.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.commons.utils.PermissionUtil;
import org.novinomad.picasso.commons.IdAware;
import org.novinomad.picasso.domain.dto.permissions.VisJsDataSet;

import java.util.*;

public interface IPermission extends IdAware<Long>, Comparable<IPermission> {

    Set<? extends IPermission> getChildren();

    String getName();

    String getDescription();

    default boolean hasChild(IPermission child) {
        return PermissionUtil.hasChild(this, child);
    }

    
    default IPermission addChild(IPermission child) {
        Set<IPermission> children = (Set<IPermission>) getChildren();

        children.add(child);

        return this;
    }

    IPermission addChild(String name, String description);

    
    default IPermission deleteChild(Long childPermissionId) {
        getChildren().removeIf(childPermission -> childPermission.getId().equals(childPermissionId));
        return this;
    }

    
    default IPermission deleteChild(IPermission childPermission) {
        getChildren().remove(childPermission);
        return this;
    }

    default boolean hasChildren() {
        return !getChildren().isEmpty();
    }

    default boolean hasParents() {
        return !getClosestParents().isEmpty();
    }

    default VisJsDataSet.Node toVisJsNode() {
        return new VisJsDataSet.Node(getId(), getName(), getDescription());
    }

    /**
     * @return Set of top level permissions related to this permission
     * */
    @JsonIgnore
    default Set<IPermission> getRoots() {
        return PermissionUtil.getRoots(this, new TreeSet<>());
    }

    default boolean hasCycle() {
        return PermissionUtil.hasCycle(this, null, null);
    }

    default boolean contains(IPermission permission) {
        if(this.equals(permission)) return  true;

        Set<IPermission> children1 = (Set<IPermission>) getChildren();
        if(children1.contains(permission)) return true;

        for (IPermission child : children1) {
            if(PermissionUtil.traverseBFS(child).containsKey(permission.getId()))
                return true;
        }
        return false;
    }

    default boolean containsAll(Set<IPermission> permissions) {

        Map<Long, Boolean> containsMap = new HashMap<>();

        permissions.forEach(p -> containsMap.put(p.getId(), false));

        if(containsMap.containsKey(getId()))
            containsMap.put(getId(), true);

        Set<IPermission> children1 = (Set<IPermission>) getChildren();

        for (IPermission child : children1) {
            if(containsMap.containsKey(child.getId()))
                containsMap.put(child.getId(), true);

            LinkedHashMap<Long, String> traverseBFS = PermissionUtil.traverseBFS(child);

            traverseBFS.keySet().forEach(k-> {
                if(containsMap.containsKey(k))
                    containsMap.put(k, true);
            });
        }
        return !containsMap.containsValue(false);
    }

    @JsonIgnore
    Set<IPermission> getClosestParents();
}
