package org.novinomad.picasso.erm.entities.base;

import org.apache.commons.lang3.NotImplementedException;

public interface ModelConvertable<M> {

    default M toModel() {
        throw new NotImplementedException();
    }
}
