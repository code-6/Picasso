package org.novinomad.picasso.domain.erm.entities.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.novinomad.picasso.commons.enums.UITheme;
import org.novinomad.picasso.domain.erm.entities.AbstractEntity;
import org.novinomad.picasso.domain.erm.entities.auth.User;

import javax.persistence.*;
import java.util.Locale;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class UserSettings extends AbstractEntity {

    private Locale locale;

    @Enumerated(EnumType.STRING)
    private UITheme uiTheme;

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private User user;

    @Override
    public String toString() {
        return super.toString().replace("}", "") +
                "locale=" + locale +
                ", uiTheme=" + uiTheme +
                ", user=" + user.getUsername() +
                '}';
    }
}
