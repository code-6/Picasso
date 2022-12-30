package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.domain.erm.entities.auth.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query(nativeQuery = true, value = """
            select p.id, p.name, p.description
            from PERMISSION_CHILDREN pc
            join permission p on pc.PARENT_ID = p.ID
            where pc.CHILD_ID = :id
            group by p.id, p.name, p.description
            order by p.name
            """)
    SortedSet<Permission> getClosestParents(@Param("id") Long id);

    @Query(nativeQuery = true, value = """
            select p.id, p.name, p.description
            from PERMISSION p
            left join PERMISSION_CHILDREN pc on p.id = pc.CHILD_ID
            where pc.PARENT_ID is null
            group by p.id, p.name, p.description
            order by p.name
            """)
    List<Permission> getRootPermissions();


    @Query(nativeQuery = true, value = """
            select p.* from PERMISSION_CHILDREN pc
            join PERMISSION p on p.id = pc.child_id
            where pc.parent_id = :parentPermissionId
            group by p.id, p.name, p.description
            """)
    Set<Permission> findByParentId(@Param("parentPermissionId") Long parentPermissionId);

    Optional<Permission> findByName(String name);
}