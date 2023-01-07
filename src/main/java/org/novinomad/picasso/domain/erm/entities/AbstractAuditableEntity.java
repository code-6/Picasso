package org.novinomad.picasso.domain.erm.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.commons.utils.CommonDateUtils;
import org.novinomad.picasso.commons.utils.SpringContextUtil;
import org.novinomad.picasso.services.auth.UserService;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
@FieldDefaults(level = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractAuditableEntity extends AbstractEntity {
    @Getter
    @Setter
    @Column(name = "CREATE_DATE")
    @DateTimeFormat(pattern = CommonDateUtils.UI_DATE_TIME)
    LocalDateTime createDate;

    @Getter
    @Setter
    @Column(name = "CREATED_BY")
    String createdBy;

    @Getter
    @Setter
    @Column(name = "LAST_UPDATE_DATE")
    @DateTimeFormat(pattern = CommonDateUtils.UI_DATE_TIME)
    LocalDateTime lastUpdateDate;

    @Getter
    @Setter
    @Column(name = "LAST_UPDATED_BY")
    String lastUpdatedBy;

    @Getter
    @Setter
    @Column(nullable = false, name = "DELETED")
    Boolean deleted = false;

    @PrePersist
    void prePersist() {
        createDate = LocalDateTime.now();
        if(createdBy == null) {
            createdBy = SpringContextUtil.getBean(UserService.class).getCurrentUserName();
        }
    }

    @PreUpdate
    void preUpdate() {
        lastUpdateDate = LocalDateTime.now();
        lastUpdatedBy = SpringContextUtil.getBean(UserService.class).getCurrentUserName();
    }

    @PreRemove
    void preDelete() {
        preUpdate();
        deleted = true;
    }

    public String getCreateDateAsString() {
        return createDate != null
                ? createDate.format(DateTimeFormatter.ofPattern(CommonDateUtils.UI_DATE_TIME))
                : "";
    }

    public String getUpdateDateAsString() {
        return lastUpdateDate != null
                ? lastUpdateDate.format(DateTimeFormatter.ofPattern(CommonDateUtils.UI_DATE_TIME))
                : "";
    }
}
