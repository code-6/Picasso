package org.novinomad.picasso.domain.erm.entities.common;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.novinomad.picasso.commons.utils.ValidationUtil;
import org.novinomad.picasso.domain.erm.entities.AbstractEntity;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Contact known nothing to which entity it belongs.
 * */
@Entity
@NoArgsConstructor
public class ContactInfo extends AbstractEntity {

    @Getter
    @Setter(AccessLevel.PROTECTED)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> emails = new HashSet<>();

    public Optional<String> getEmailOptional() {
        return emails.stream().findFirst();
    }

    public boolean hasEmail() {
        return getEmailOptional().isPresent();
    }

    public ContactInfo addEmail(@NotNull String email) {
        if(ValidationUtil.isValidEmail(email)) {
            emails.add(email);
            return this;
        } else {
            throw new IllegalArgumentException("email " + email + " is invalid");
        }
    }

    public ContactInfo deleteEmail(@NotNull String email) {
        if(StringUtils.isNotBlank(email)) {
            emails.removeIf(e -> e.equalsIgnoreCase(email));
        }
        return this;
    }

    @Override
    public String toString() {
        return super.toString().replace("}", "") +
                "emails=" + emails +
                '}';
    }
}
