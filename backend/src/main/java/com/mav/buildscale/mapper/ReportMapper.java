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

    @Mapping(target = "report", ignore = true)
    Tag mapTagDtoToTag(final TagDto tagDto);

    @Mapping(target = "report", ignore = true)
    Task mapTaskDtoToTask(final TaskDto taskDto);

    @Mapping(target = "report", ignore = true)
    @Mapping(target = "testFailures", ignore = true)
    Test mapTestDtoToTest(final TestDto testDto);

    @AfterMapping
    default void afterMapping(@MappingTarget final Test test, final TestDto testDto) {
        testDto.getFailures().stream()
                .map(this::mapTestFailureDtoToTestFailure)
                .forEach(test::addTestFailure);
    }

    @Mapping(target = "test", ignore = true)
    TestFailure mapTestFailureDtoToTestFailure(final TestFailureDto testFailureDto);
}
