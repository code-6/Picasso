package org.novinomad.picasso.dto.gantt;

import java.util.Collection;
import java.util.List;

public interface ITask {
    List<Task> getDependencies();

    List<Task> getChildren();

    Task getParent();

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
