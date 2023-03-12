package com.mav.buildscale.mapper;

import com.mav.buildscale.model.CreateReport;
import com.mav.buildscale.model.CreateTag;
import com.mav.buildscale.model.CreateTask;
import com.mav.buildscale.model.Report;
import com.mav.buildscale.model.Tag;
import com.mav.buildscale.model.Task;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReportMapper {

    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "tags", ignore = true)
    Report createReportToReport(final CreateReport report);

    @AfterMapping
    default void afterMapping(@MappingTarget final Report report, final CreateReport createReport) {
        createReport.getTasks().stream()
                .map(this::createTaskToTask)
                .forEach(report::addTask);

        createReport.getTags().stream()
                .map(this::createTagToTag)
                .forEach(report::addTag);
    }

    @Mapping(target = "report", ignore = true)
    Task createTaskToTask(final CreateTask task);

    @Mapping(target = "report", ignore = true)
    Tag createTagToTag(final CreateTag tag);
}
