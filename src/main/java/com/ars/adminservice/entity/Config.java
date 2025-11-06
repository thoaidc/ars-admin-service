package com.ars.adminservice.entity;

import com.dct.config.entity.AbstractAuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.DynamicUpdate;
import java.io.Serializable;

@Entity
@DynamicUpdate
@Table(name = "config")
public class Config extends AbstractAuditingEntity implements Serializable {

    @Column(name = "shop_id")
    private Integer shopId;

    @Size(max = 50)
    @Column(name = "code")
    private String code;

    @Column(name = "value")
    private String value;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
