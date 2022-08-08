package org.novinomad.picasso.services.impl;


import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.services.ILayoutSideToggleHolder;

@Slf4j
public class LayoutSideToggleHolder implements ILayoutSideToggleHolder {

    private boolean hidden = true;

    @Override
    public void toggle() {
        hidden = !hidden;
        log.debug("side-nav should be {} ", (hidden ? "hidden" : "visible"));
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }
}
