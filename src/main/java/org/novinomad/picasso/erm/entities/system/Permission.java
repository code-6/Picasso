package org.novinomad.picasso.erm.entities.system;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.commons.utils.SpringContextUtil;
import org.novinomad.picasso.erm.dto.VisJsDataSet;
import org.novinomad.picasso.erm.entities.base.AbstractEntity;
import org.novinomad.picasso.erm.entities.base.IdAware;
import org.novinomad.picasso.repositories.jpa.PermissionRepository;
import org.novinomad.picasso.services.impl.PermissionService;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Permission extends AbstractEntity implements Comparable<Permission> {
    @Column(unique = true, nullable = false)
    private String name;

    @Column(length = 400)
    private String description;

    @Transient
    @JsonIgnore
    private boolean getChildrenFromDb = true;

    @Transient
    @JsonIgnore
    private boolean getParentsFromDb = true;


    @Setter
    @Getter
    @JsonIgnore
    @Transient
    private boolean beingVisited;

    @Setter
    @Getter
    @JsonIgnore
    @Transient
    private boolean visited;

    public Permission(String name) {
        this.name = name;
    }

    public Permission(String name, String description) {
        this(name);
        this.description = description;
    }

    @ManyToMany
    @JoinTable(name = "user_permissions",
            joinColumns = @JoinColumn(name = "permission_id"),
            inverseJoinColumns = @JoinColumn(name = "user_name"))
    private Set<User> users = new HashSet<>();

    // permission may be child of many others permissions
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "permission_children",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id"))
    private Set<Permission> children = new TreeSet<>();

    @JsonIgnore
    @Transient
    private Set<Permission> parents = new TreeSet<>();

    public Set<Permission> getChildren() {
        if(getChildrenFromDb)
            children = SpringContextUtil.getBean(PermissionRepository.class).findByParentId(this.id);

        getChildrenFromDb = false;

        return children;
    }

    /**
     * @throws CommonRuntimeException in case if cycle reference is detected
     * */
    public Permission addChild(Permission child) {

        LinkedHashMap<Long, String> traversedPermissions = PermissionService.traverseBFS(this);

        if(traversedPermissions.containsValue(child.getName()))
            throw new CommonRuntimeException("{} already has {} as (no-directed) child. There is no sense in inheritance", getName(), child.getName());

        getChildren().add(child);

        if(hasCycle())
            throw new CommonRuntimeException("add child {} to parent {} forbidden because cycle reference detected", child.getName(), getName());

        return this;
    }

    public Permission addChild(String name, String description) {
        return addChild(new Permission(name, description));
    }

    public Permission deleteChild(Long childPermissionId) {
        getChildren().removeIf(childPermission -> childPermission.getId().equals(childPermissionId));
        return this;
    }

    public boolean hasChildren() {
        return !getChildren().isEmpty();
    }

    @JsonIgnore
    public Set<Permission> getClosestParents() {
        if(getParentsFromDb)
            parents = new TreeSet<>(SpringContextUtil.getBean(PermissionRepository.class).getClosestParents(this.id));

        getParentsFromDb = false;

        return parents;
    }

    public boolean hasParents() {
        return !getClosestParents().isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Permission that = (Permission) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", users=" + users.stream().map(User::getUserName).toList() +
                ", children=" + children.stream().map(Permission::getName).toList() +
                '}';
    }

    @Override
    public int compareTo(Permission permission) {
        return this.name.compareTo(permission.name);
    }

    public VisJsDataSet.Node toVisJsNode() {
        return new VisJsDataSet.Node(id, name, description);
    }

    /**
     * @return Set of top level permissions related to this permission
     * */
    @JsonIgnore
    public Set<Permission> getRoots() {
        return PermissionService.getRoots(this, new TreeSet<>());
    }

    public boolean hasCycle() {
        return PermissionService.hasCycle(this);
    }

    public boolean contains(Permission permission) {
        if(this.equals(permission)) return  true;

        Set<Permission> children1 = getChildren();
        if(children1.contains(permission)) return true;

        for (Permission child : children1) {
            if(PermissionService.traverseBFS(child).containsKey(permission.getId()))
                return true;
        }
        return false;
    }

    public boolean containsAll(Set<Permission> permissions) {

        Map<Long, Boolean> containsMap = new HashMap<>();

        permissions.forEach(p -> containsMap.put(p.getId(), false));

        if(containsMap.containsKey(getId()))
            containsMap.put(getId(), true);

        Set<Permission> children1 = getChildren();

        for (Permission child : children1) {
            if(containsMap.containsKey(child.getId()))
                containsMap.put(child.getId(), true);

            LinkedHashMap<Long, String> traverseBFS = PermissionService.traverseBFS(child);

            traverseBFS.keySet().forEach(k-> {
                if(containsMap.containsKey(k))
                    containsMap.put(k, true);
            });
        }
        return !containsMap.containsValue(false);
    }
}
