package org.novinomad.picasso.commons;

import org.novinomad.picasso.erm.entities.base.IdAware;
import org.novinomad.picasso.erm.entities.system.Permission;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface ICrud<Id, Entity extends IdAware<Id>> {

    Entity save(Entity entity);

    @Transactional
    default List<Entity> save(Iterable<Entity> entities) {
        List<Entity> savedEntities = new ArrayList<>();
        for (Entity entity : entities) {
            savedEntities.add(save(entity));
        }
        return savedEntities;
    }

    void deleteById(Id id);

    @Transactional
    default void deleteById(Iterable<Id> ids) {
        for (Id id : ids) {
            deleteById(id);
        }
    }

    @Transactional
    default void delete(Entity... entity) {
        for (Entity e : entity) {
            deleteById(e.getId());
        }
    }

    List<Entity> get(Id... ids);

    Optional<Entity> get(Id id);
}
