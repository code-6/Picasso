package org.novinomad.picasso.domain.erm.entities;

public interface SoftDeletableEntity {
    boolean isDeleted();
    boolean deleteSoft();
}
