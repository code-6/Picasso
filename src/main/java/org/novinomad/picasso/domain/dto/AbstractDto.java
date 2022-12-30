package org.novinomad.picasso.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.novinomad.picasso.conversion.EntityConvertable;
import org.novinomad.picasso.domain.erm.entities.AbstractEntity;
import org.novinomad.picasso.commons.IdAware;

@Getter
@Setter
public abstract class AbstractDto implements EntityConvertable<AbstractEntity>, IdAware<Long> {

    protected Long id;

}
