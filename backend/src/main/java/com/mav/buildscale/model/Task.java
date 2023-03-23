package com.mav.buildscale.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "BES_TASK")
public class Task extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "REPORT_OID")
    private Report report;
    
    @Column(name = "PATH")
    private String path;

    @Column(name = "STARTTIME")
    private Long startTime;

    @Column(name = "ENDTIME")
    private Long endTime;

    @Column(name = "DURATION")
    private Long durationInMillis;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.EXECUTED;

    @Column(name = "INCREMENTAL")
    private boolean isIncremental;

    @Column(name = "MESSAGES")
    private String messages;
}
