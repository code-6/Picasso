package org.novinomad.picasso.commons;

import org.novinomad.picasso.domain.entities.base.AbstractEntity;
import org.novinomad.picasso.exceptions.base.PicassoException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface ICrud<T extends AbstractEntity> {

    T save(T t) throws PicassoException;

    void delete(Long id) throws PicassoException;

    /**
     * If already exists -> return existent else -> create new
     */
    default T createOrGet(T t) throws PicassoException {
        return get(t.getId()).orElse(save(t));
    }

    default void delete(T t) throws PicassoException {
        delete(t.getId());
    }

    default Optional<T> get(Long id) {
        return Optional.empty();
    }

    default List<T> get() {
        return Collections.emptyList();
    }
}
