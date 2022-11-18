package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.erm.entities.system.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}