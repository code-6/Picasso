package org.novinomad.picasso.erm.dto.base;

import lombok.Getter;
import lombok.Setter;
import org.novinomad.picasso.erm.entities.base.AbstractEntity;
import org.novinomad.picasso.erm.entities.base.IdAware;

@Getter
@Setter
public abstract class AbstractModel implements EntityConvertable<AbstractEntity>, IdAware<Long> {

    protected Long id;

}
