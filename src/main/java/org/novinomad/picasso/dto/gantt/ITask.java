package org.novinomad.picasso.dto.gantt;

import org.novinomad.picasso.domain.IdAware;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public interface ITask extends IdAware<Long> {
    List<Task> getDependencies();

    List<Task> getChildren();

    Task getParent();

    default boolean hasChildren() {
        return !CollectionUtils.isEmpty(getChildren());
    }

    default boolean hasParent() {
        return Objects.nonNull(getParent());
    }

    default boolean hasDependencies() {
        return !CollectionUtils.isEmpty(getDependencies());
    }

    default void addChild(Task child) {
        getChildren().add(child);
    }

    default void addChild(Collection<Task> children) {
        getChildren().addAll(children);
    }

    default void addDependency(Task dependency) {
        getDependencies().add(dependency);
    }

    default void addDependency(Collection<Task> dependencies) {
        getDependencies().addAll(dependencies);
    }
}
