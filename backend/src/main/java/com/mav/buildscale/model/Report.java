package com.mav.buildscale.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "BES_REPORT")
@EntityListeners(AuditingEntityListener.class)
public class Report extends AbstractEntity {

    @CreatedDate
    @Column(name = "CREATED")
    private LocalDateTime created;

    @Column(name = "PROJECT")
    private String project;

    @Column(name = "HOSTNAME")
    private String hostname;
    
    @Column(name = "TASK_EXEC_REQ")
    private String taskExecutionRequest;

    @Column(name = "DURATION")
    private Long durationInMillis;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status = Status.SUCCESS;


    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Test> tests = new ArrayList<>();

    public void addTag(final Tag tag) {
        tag.setReport(this);
        tags.add(tag);
    }

    public void addTask(final Task task) {
        task.setReport(this);
        tasks.add(task);
    }

    public void addTest(final Test test) {
        test.setReport(this);
        tests.add(test);
    }
}
