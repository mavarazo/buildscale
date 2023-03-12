package com.mav.buildscale.controller;

import com.mav.buildscale.mapper.ReportMapper;
import com.mav.buildscale.model.CreateReport;
import com.mav.buildscale.model.Report;
import com.mav.buildscale.model.Tag;
import com.mav.buildscale.model.Task;
import com.mav.buildscale.repository.ReportRepository;
import com.mav.buildscale.repository.TagRepository;
import com.mav.buildscale.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Controller
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportController {

    private final ReportRepository reportRepository;
    private final TaskRepository taskRepository;
    private final TagRepository tagRepository;

    private final ReportMapper reportMapper;

    @QueryMapping
    private List<Report> recentReports(@Argument final Integer size, @Argument final Integer page) {
        return reportRepository
                .findAll(Pageable.ofSize(size).withPage(page))
                .toList();
    }

    @SchemaMapping
    private List<Task> tasks(final Report report) {
        return taskRepository.findByReportOid(report.getOid());
    }

    @SchemaMapping
    private List<Tag> tags(final Report report) {
        return tagRepository.findByReportOid(report.getOid());
    }

    @MutationMapping(value = "createReport")
    private Report createReport(@Argument final CreateReport createReport) {
        final Report report = new Report();
        report.setProject(createReport.getProject());
        //reportMapper.createReportToReport(createReport)
        return reportRepository.save(report);
    }
}
