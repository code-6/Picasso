package org.novinomad.picasso.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.novinomad.picasso.entities.base.IdAware;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppSettings implements IdAware<String> {

    @Transient
    @JsonIgnore
    public static final AppSettings TEST_DATA_INITIALIZED = new AppSettings("test-data-initialized", "false", Boolean.class.getName());

    @Id
    private String settingKey;

    @Column(nullable = false)
    private String settingValue;

    private String settingValueType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AppSettings that = (AppSettings) o;
        return settingKey != null && Objects.equals(settingKey, that.settingKey);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String getId() {
        return settingKey;
    }

    public AppSettings value(String value) {
        this.setSettingValue(value);
        return this;
    }
}
