package org.novinomad.picasso.commons;

import org.novinomad.picasso.entities.base.AbstractEntity;
import org.novinomad.picasso.exceptions.base.BaseException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface ICrud<T extends AbstractEntity> {

    T save(T t) throws BaseException;

    void delete(Long id) throws BaseException;

    default void delete(T t) throws BaseException {
        delete(t.getId());
    }

    default Optional<T> get(Long id) {
        return Optional.empty();
    }

    default List<T> get(Long ... ids) {
        return Collections.emptyList();
    }

    default List<T> get() {
        return Collections.emptyList();
    }
}
