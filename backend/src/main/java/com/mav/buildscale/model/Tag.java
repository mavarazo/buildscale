package com.mav.buildscale.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "BES_TAG")
public class Tag extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "REPORT_OID")
    private Report report;

    @Column(name = "KEY_VAL")
    private String key;

    @Column(name = "VALUE_VAL")
    private String value;
}
