package org.novinomad.picasso.services;

public interface ILayoutSideToggleHolder {
    void toggle();

    boolean isHidden();

    default boolean sideShouldBeHidden() {
        return !isHidden();
    }
}
