package com.mav.buildscale.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "BES_TEST")
public class Test extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "REPORT_OID")
    private Report report;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CLASS_NAME")
    private String className;

    @Column(name = "DURATION")
    private Long durationInMillis;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private TestStatus status = TestStatus.SUCCESS;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestFailure> testFailures = new ArrayList<>();

    public void addTestFailure(final TestFailure testFailure) {
        testFailure.setTest(this);
        testFailures.add(testFailure);
    }
}
