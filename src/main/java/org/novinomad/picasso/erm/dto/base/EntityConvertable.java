package org.novinomad.picasso.erm.dto.base;

import org.apache.commons.lang3.NotImplementedException;

public interface EntityConvertable<E> {
    default E toEntity() {
        throw new NotImplementedException();
    }
}
