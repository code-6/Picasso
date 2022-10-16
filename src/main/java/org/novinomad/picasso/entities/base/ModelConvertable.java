package org.novinomad.picasso.entities.base;

import org.apache.commons.lang3.NotImplementedException;

public interface ModelConvertable<M> {

    default M toModel() {
        throw new NotImplementedException();
    }
}
