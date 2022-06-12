package org.novinomad.picasso.commons;

import org.novinomad.picasso.domain.entities.base.AbstractEntity;
import org.novinomad.picasso.exceptions.base.PicassoException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ICrud<T extends AbstractEntity> {

    T save( T t ) throws PicassoException;

    /**
     * If already exists -> return existent else -> create new
     * */
    default T createOrGet(T t) throws PicassoException {
        return get(t.getId()).orElse(save(t));
    }

    void delete(Long id) throws PicassoException;

    default void delete(T t) throws PicassoException {
        delete(t.getId());
    }

    Optional<T> get(Long id);

    List<T> get();
}
