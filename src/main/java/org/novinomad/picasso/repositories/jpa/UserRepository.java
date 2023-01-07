package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.domain.erm.entities.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("delete from User u where u.username = :username")
    void deleteByUsername(String username);

    @Query("select u from User u where u.username in (:usernames)")
    List<User> findAllByUsername(@Param("usernames") Set<String> usernames);

    Optional<User> findByUsername(String username);

    @Query(value = """
            select distinct u.*
            from user_permissions up where up.PERMISSION_ID = :permissionId
            join users u on up.USER_NAME = u.USERNAME
            """, nativeQuery = true)
    Set<User> findAllByPermissionId(@Param("permissionId") Long permissionId);
}