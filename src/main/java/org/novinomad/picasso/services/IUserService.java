package org.novinomad.picasso.services;

import java.util.Locale;

public interface IUserService {
    default Locale getCurrentUserLocale() {
        return Locale.ENGLISH;
    }
}
