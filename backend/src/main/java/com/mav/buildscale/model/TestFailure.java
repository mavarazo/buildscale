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
@Table(name = "BES_TEST_FAILURE")
public class TestFailure extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "TEST_OID")
    private Test test;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "STACKTRACE")
    private String stacktrace;
}
