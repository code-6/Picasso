package org.novinomad.picasso.conversion;

import org.apache.commons.lang3.NotImplementedException;

public interface EntityConvertable<E> {
    default E toEntity() {
        throw new NotImplementedException();
    }
}