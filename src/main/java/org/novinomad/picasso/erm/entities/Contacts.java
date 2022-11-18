package org.novinomad.picasso.erm.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.novinomad.picasso.commons.CommonArrayList;
import org.novinomad.picasso.erm.entities.base.AbstractEntity;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Contacts extends AbstractEntity {

    @ElementCollection
    private List<String> emails = new CommonArrayList<>();
}
