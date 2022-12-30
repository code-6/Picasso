package org.novinomad.picasso.services;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.commons.Crud;
import org.novinomad.picasso.commons.IdAware;
import org.slf4j.event.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class AbstractCrudCacheService<ID, ENTITY extends IdAware<ID>> implements Crud<ID, ENTITY> {

    protected final JpaRepository<ENTITY, ID> repository;

    protected final LoadingCache<ID, ENTITY> CACHE;

    protected AbstractCrudCacheService(JpaRepository<ENTITY, ID> repository) {
        this.repository = repository;

        CACHE = Caffeine.newBuilder()
                .build(id -> repository.findById(id).orElse(null));
    }

    // cache update
    @Override
    public ENTITY save(ENTITY entity) {
        entity = repository.save(entity);

        CACHE.put(entity.getId(), entity);

        return entity;
    }

    @Override
    public List<ENTITY> save(Collection<ENTITY> entities) {
        List<ENTITY> savedEntities = repository.saveAll(entities);

        savedEntities.forEach(e -> CACHE.put(e.getId(), e));

        return savedEntities;
    }

    @Override
    @Transactional
    public void deleteById(ID id) {
        repository.deleteById(id);

        CACHE.invalidate(id);
    }

    @Override
    @Transactional
    public void deleteById(Collection<ID> ids) {
        repository.deleteAllById(ids);

        CACHE.invalidateAll(ids);
    }

    @Override
    @Transactional
    public void delete(ENTITY entity) {
        repository.delete(entity);

        CACHE.invalidate(entity.getId());
    }

    @Override
    @Transactional
    public void delete(Collection<ENTITY> entities) {
        repository.deleteAll(entities);

        entities.forEach(e -> CACHE.invalidate(e.getId()));
    }

    // cache read

    @Override
    public List<ENTITY> get() {
        if(CACHE.asMap().isEmpty()) {
            repository.findAll().forEach(e -> CACHE.put(e.getId(), e));
        }
        return new ArrayList<>(CACHE.asMap().values());
    }

    @Override
    public Optional<ENTITY> getById(ID id) {

        return Optional.ofNullable(CACHE.get(id));
    }

    @Override
    public List<ENTITY> getById(Collection<ID> ids) {
        List<ENTITY> allById;
        if(CACHE.asMap().isEmpty()) {
            allById = repository.findAllById(ids);

            allById.forEach(e -> CACHE.put(e.getId(), e));
        } else {
            allById = new ArrayList<>();
            ids.forEach(id -> Optional.ofNullable(CACHE.get(id)).ifPresent(allById::add));
        }
        return allById;
    }
}
