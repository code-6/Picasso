package org.novinomad.picasso.domain.erm.entities.auth;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.novinomad.picasso.commons.utils.SpringContextUtil;
import org.novinomad.picasso.domain.dto.permissions.VisJsDataSet;
import org.novinomad.picasso.domain.erm.entities.AbstractEntity;
import org.novinomad.picasso.repositories.jpa.PermissionRepository;
import org.novinomad.picasso.repositories.jpa.UserRepository;
import org.novinomad.picasso.services.auth.PermissionServiceImpl;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;


@Entity
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Permission extends AbstractEntity implements IPermission {
    @Getter
    @Setter
    @Column(unique = true, nullable = false)
    private String name;

    @Getter
    @Setter
    @Column(length = 400)
    private String description;

//    @Transient
//    @JsonIgnore
//    private boolean getChildrenFromDb = true;
//
//    @Transient
//    @JsonIgnore
//    private boolean getUsersFromDb = true;

    @Transient
    @JsonIgnore
    private boolean getParentsFromDb = true;

    public Permission(String name) {
        this.name = name;
    }

    public Permission(String name, String description) {
        this(name);
        this.description = description;
    }

    @Getter
    @Setter
    @ManyToMany
    @JoinTable(name = "user_permissions",
            joinColumns = @JoinColumn(name = "permission_id"),
            inverseJoinColumns = @JoinColumn(name = "user_name"))
//    @Fetch(FetchMode.SUBSELECT)
    private Set<User> users = new HashSet<>();

    @Setter
    // permission may be child of many others permissions
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "permission_children",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id"))
//    @Fetch(FetchMode.SUBSELECT)
    private Set<Permission> children = new TreeSet<>();


    @JsonIgnore
    @Transient
    private Set<Permission> parents = new TreeSet<>();

    @Override
    public Set<? extends IPermission> getChildren() {
//        if (getChildrenFromDb) {
//            this.children = SpringContextUtil.getBean(PermissionRepository.class).findByParentId(this.id);
//            getChildrenFromDb = false;
//        }
        return children;
    }

//    public Set<User> getUsers() {
//        if (getUsersFromDb) {
//            this.users = SpringContextUtil.getBean(UserRepository.class).findAllByPermissionId(this.id);
//            getUsersFromDb = false;
//        }
//        return users;
//    }

    @Override
    public IPermission addChild(String name, String description) {

        children.add(new Permission(name, description));

        return this;
    }

    @Override
    @JsonIgnore
    public Set<IPermission> getClosestParents() {
        if (getParentsFromDb) {
            parents = new TreeSet<>(SpringContextUtil.getBean(PermissionRepository.class)
                    .getClosestParents(this.id));

            getParentsFromDb = false;
        }
        return parents
                .stream()
                .map(IPermission.class::cast)
                .collect(Collectors.toCollection(TreeSet::new));
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
        return super.toString().replace("}", "") +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", users=" + users.parallelStream().collect(Collectors.toMap(User::getId, User::getUsername)) +
                ", children=" + children +
                '}';
    }

    @Override
    public int compareTo(IPermission permission) {
        return this.name.compareTo(permission.getName());
    }
}
