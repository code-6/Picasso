package org.novinomad.picasso.commons;

import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.slf4j.event.Level;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

public interface Crud<ID, ENTITY extends IdAware<ID>> {

    ENTITY save(@NotNull ENTITY entity);

    default List<ENTITY> save(@NotEmpty Collection<ENTITY> entities) {

        List<ENTITY> savedEntities = new ArrayList<>();
        if(!CollectionUtils.isEmpty(entities)) {
            for (ENTITY entity : entities) {
                savedEntities.add(save(entity));
            }
        }
        return savedEntities;
    }

    void deleteById(@NotNull ID id);

    default void deleteById(@NotEmpty Collection<ID> ids) {
        if(!CollectionUtils.isEmpty(ids)) {
            for (ID id : ids) {
                deleteById(id);
            }
        }
    }

    default void delete(ENTITY entity) {
        deleteById(entity.getId());
    }

    default void delete(@NotEmpty Collection<ENTITY> entities) {
        if(!CollectionUtils.isEmpty(entities)) {
            for (ENTITY e : entities) {
                deleteById(e.getId());
            }
        }
    }

    List<ENTITY> getById(@NotEmpty Collection<ID> ids);

    List<ENTITY> get();

    Optional<ENTITY> getById(@NotNull ID id);

    /**
     * @throws NoSuchElementException if entry not present by provided id
     * */
    default ENTITY getByIdOrElseThrow(@NotNull ID id) {
        return getById(id)
                .orElseThrow(() -> new NoSuchElementException("entry with id = " + id + " not found or does not exist"));
    }
}
