package org.novinomad.picasso.domain.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.novinomad.picasso.domain.erm.entities.auth.Permission;
import org.novinomad.picasso.domain.erm.entities.common.ContactInfo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto implements Serializable {
    private Long id;
    private String username;
    private Locale locale;
    @Builder.Default
    private Set<Permission> permissions = new HashSet<>();
    @Builder.Default
    private ContactInfo contactInfo = new ContactInfo();
}
