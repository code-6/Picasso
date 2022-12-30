package org.novinomad.picasso.domain.erm.entities.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.novinomad.picasso.commons.IdAware;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class GuiAccessConfig implements IdAware<String> {

    @Id
    private String elementId;

    @Column(length = 400)
    private String description;

    /**
     * Defines who can see this element on UI.
     * */
    @OneToMany
    @JoinTable(name = "gui_element_permissions",
            joinColumns = @JoinColumn(name = "gui_element_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions = new HashSet<>();

    @Override
    public String getId() {
        return elementId;
    }

    @Override
    public String toString() {
        return "GuiAccessConfig{" +
                "elementId='" + elementId + '\'' +
                ", description='" + description + '\'' +
                ", permissions=" + permissions.stream().collect(Collectors.toMap(Permission::getId, Permission::getName)) +
                '}';
    }
}
