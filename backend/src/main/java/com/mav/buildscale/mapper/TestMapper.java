package com.mav.buildscale.mapper;

import com.mav.buildscale.api.model.TestDto;
import com.mav.buildscale.api.model.TestFailureDto;
import com.mav.buildscale.model.Test;
import com.mav.buildscale.model.TestFailure;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TestMapper {

    @Mapping(target = "report", ignore = true)
    @Mapping(target = "testFailures", ignore = true)
    Test mapTestDtoToTest(final TestDto testDto);

    @Mapping(target = "id", source = "oid")
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
