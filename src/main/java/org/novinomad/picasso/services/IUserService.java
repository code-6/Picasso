package org.novinomad.picasso.services;

import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.erm.entities.system.User;

import java.util.Locale;

public interface IUserService extends ICrud<String, User> {
    default Locale getCurrentUserLocale() {
        return Locale.ENGLISH;
    }
}
