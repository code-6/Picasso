package org.novinomad.picasso.services.auth;

import org.novinomad.picasso.commons.Crud;
import org.novinomad.picasso.commons.utils.ValidationUtil;
import org.novinomad.picasso.domain.erm.entities.auth.User;

import java.util.*;

public interface UserService extends Crud<Long, User> {
    default Locale getCurrentUserLocale() {
        return Locale.ENGLISH;
    }

    User createUser(User user);

    default User createUser(String email) {
        if(!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException(email + " is not a valid email address");
        }
        return createUser(new User().addEmail(email));
    }

    void deleteByUserName(String name);

    List<User> get(Set<String> usernames);

    Optional<User> get(String name);

    User getCurrentUser();

    default String getCurrentUserName() {
        return "dummy-user";
    }
}
