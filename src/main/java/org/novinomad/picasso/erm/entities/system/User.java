package org.novinomad.picasso.erm.entities.system;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.novinomad.picasso.erm.entities.Contacts;
import org.novinomad.picasso.erm.entities.base.IdAware;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
@JsonIdentityInfo(generator=ObjectIdGenerators.StringIdGenerator.class, property = "userName")
public class User implements IdAware<String> {

    @Id
    private String userName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToMany(mappedBy = "users")
    private Set<Permission> permissions = new HashSet<>();

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Contacts contacts;

    @Override
    public String getId() {
        return userName;
    }

    public void addEmail(String email) {
        if(contacts == null) contacts = new Contacts();

        contacts.getEmails().add(email);
    }

    public void deleteEmail(String email) {
        if(contacts != null) {
            contacts.getEmails().remove(email);
        }
    }

    public Optional<String> getEmail() {
        if(contacts != null) {
            List<String> emails = contacts.getEmails();
            if(!emails.isEmpty()) {
                return Optional.of(emails.get(0));
            }
        }
        return Optional.empty();
    }

    public List<String> getEmails() {
        return contacts == null ? Collections.emptyList() : contacts.getEmails();
    }
}
