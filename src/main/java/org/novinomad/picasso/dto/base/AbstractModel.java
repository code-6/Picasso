package org.novinomad.picasso.dto.base;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.NotImplementedException;
import org.novinomad.picasso.entities.base.AbstractEntity;
import org.novinomad.picasso.entities.base.EntityConvertable;
import org.novinomad.picasso.entities.base.IdAware;

@Getter
@Setter
public abstract class AbstractModel implements EntityConvertable<AbstractEntity>, IdAware<Long> {

    protected Long id;

}
