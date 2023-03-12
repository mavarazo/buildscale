package com.mav.buildscale.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "BS_REPORT")
@EntityListeners(AuditingEntityListener.class)
public class Report extends AbstractEntity {

    @Column(name = "PROJECT")
    private String project;

    @Column(name = "HOSTNAME")
    private String hostname;

    @Column(name = "DURATION")
    private Long durationInMillis;

    @CreatedDate
    @Column(name = "CREATED")
    private LocalDateTime created;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags = new ArrayList<>();

    public void addTask(final Task task) {
        task.setReport(this);
        tasks.add(task);
    }

    public void addTag(final Tag tag) {
        tag.setReport(this);
        tags.add(tag);
    }
}
