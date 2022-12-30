package org.novinomad.picasso.domain.erm.entities.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.novinomad.picasso.commons.utils.PasswordUtil;
import org.novinomad.picasso.domain.erm.entities.common.ContactInfo;
import org.novinomad.picasso.domain.erm.entities.AbstractEntity;
import org.novinomad.picasso.domain.erm.entities.common.UserSettings;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends AbstractEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Getter(AccessLevel.PROTECTED)
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    private String password;

    @OneToOne(mappedBy = "user")
    private UserSettings settings;

    @ManyToMany(mappedBy = "users")
    private Set<Permission> permissions = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    private ContactInfo contactInfo = new ContactInfo();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User addEmail(String email) {
        contactInfo.addEmail(email);
        return this;
    }

    public User deleteEmail(String email) {
        contactInfo.deleteEmail(email);
        return this;
    }

    public User addPermission(Permission permission) {
        permissions.add(permission);
        return this;
    }

    public User removePermission(Permission permission) {
        permissions.remove(permission);
        return this;
    }

    public boolean hasEmail() {
        return contactInfo.hasEmail();
    }

    public Optional<String> getEmailOptional() {
        return contactInfo.getEmailOptional();
    }

    @Override
    public String toString() {
        return super.toString().replace("}", "") +
                ", username='" + username + '\'' +
                ", settings=" + settings +
                ", permissions=" + permissions.stream().collect(Collectors.toMap(Permission::getId, Permission::getName)) +
                ", contactInfo=" + contactInfo +
                '}';
    }
}
