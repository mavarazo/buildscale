package com.mav.buildscale.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Getter
@MappedSuperclass
abstract class AbstractEntity {

    @Id
    @Column(name = "OID", updatable = false)
    private String oid;

    @PrePersist
    private void onPrePersist() {
        if (!StringUtils.hasText(oid)) {
            oid = UUID.randomUUID().toString();
        }
    }
}
