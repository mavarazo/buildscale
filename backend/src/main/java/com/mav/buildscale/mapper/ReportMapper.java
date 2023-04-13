package com.mav.buildscale.mapper;

import com.mav.buildscale.api.model.ReportDto;
import com.mav.buildscale.api.model.TagDto;
import com.mav.buildscale.api.model.TaskDto;
import com.mav.buildscale.api.model.TestDto;
import com.mav.buildscale.api.model.TestFailureDto;
import com.mav.buildscale.model.Report;
import com.mav.buildscale.model.Tag;
import com.mav.buildscale.model.Task;
import com.mav.buildscale.model.Test;
import com.mav.buildscale.model.TestFailure;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.nonNull;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReportMapper {

    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "tests", ignore = true)
    Report mapReportDtoToReportDo(final ReportDto reportDto);

    @AfterMapping
    default void afterMapping(@MappingTarget final Report report, final ReportDto reportDto) {
        reportDto.getTags().stream()
                .map(this::mapTagDtoToTag)
                .forEach(report::addTag);

        reportDto.getTasks().stream()
                .map(this::mapTaskDtoToTask)
                .forEach(report::addTask);

        reportDto.getTests().stream()
                .map(this::mapTestDtoToTest)
                .forEach(report::addTest);
    }

    @Mapping(target = "id", source = "oid")
    ReportDto mapReportToReportDto(final Report report);

    @Mapping(target = "report", ignore = true)
    Tag mapTagDtoToTag(final TagDto tagDto);

    TagDto mapTagToTagDto(Tag tag);

    @Mapping(target = "report", ignore = true)
    @Mapping(target = "messages", source = ".")
    @Mapping(target = "incremental", source = "isIncremental")
    Task mapTaskDtoToTask(final TaskDto taskDto);

    default String mapMessagesToMessages(final TaskDto taskDto) {
        return nonNull(taskDto.getMessages())
                ? String.join("; ", taskDto.getMessages())
                : null;
    }

    @Mapping(target = "messages", source = ".")
    @Mapping(target = "isIncremental", source = "incremental")
    TaskDto mapTaskToTaskDto(final Task task);

    default List<String> mapMessagesToMessages(final Task task) {
        return nonNull(task.getMessages())
                ? Arrays.stream(task.getMessages().split("; ")).toList()
                : null;
    }

    @Mapping(target = "report", ignore = true)
    @Mapping(target = "testFailures", ignore = true)
    Test mapTestDtoToTest(final TestDto testDto);

    @Mapping(target = "failures", source = "testFailures")
    TestDto mapTestToTestDto(final Test test);

    @AfterMapping
    default void afterMapping(@MappingTarget final Test test, final TestDto testDto) {
        testDto.getFailures().stream()
                .map(this::mapTestFailureDtoToTestFailure)
                .forEach(test::addTestFailure);
    }

    @Mapping(target = "test", ignore = true)
    TestFailure mapTestFailureDtoToTestFailure(final TestFailureDto testFailureDto);

    TestFailureDto mapTestFailureToTestFailureDto(final TestFailure testFailure);
}