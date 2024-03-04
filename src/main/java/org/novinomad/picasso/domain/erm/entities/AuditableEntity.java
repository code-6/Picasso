package org.novinomad.picasso.domain.erm.entities;

import org.novinomad.picasso.commons.utils.SpringContextUtil;
import org.novinomad.picasso.services.auth.UserService;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public interface AuditableEntity {

    void prePersist();

    void preUpdate();

    void preDelete();

    LocalDateTime getCreateDate();

    String getCreatedBy();

    LocalDateTime getLastUpdateDate();

    String getLastUpdatedBy();

    void setCreateDate(LocalDateTime createDate);

    void setCreatedBy(String createdBy);

    void setLastUpdateDate(LocalDateTime lastUpdateDate);

    void setLastUpdatedBy(String lastUpdatedBy);

}
