package com.mav.buildscale.mapper;

import com.mav.buildscale.api.model.ReportDto;
import com.mav.buildscale.model.Report;
import com.mav.buildscale.model.TaskStatus;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = {TagMapper.class, TaskMapper.class, TestMapper.class})
public interface ReportMapper {

    Report mapReportDtoToReportDo(final ReportDto reportDto);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "id", source = "oid")
    ReportDto mapReportToReportDto(final Report report);

    @AfterMapping
    default void afterMapping(@MappingTarget final ReportDto reportDto, final Report report) {
        final boolean hasFailedTask = report.getTasks()
                .stream()
                .anyMatch(t -> TaskStatus.FAILED.equals(t.getStatus()));
        if (hasFailedTask) {
            reportDto.status(ReportDto.StatusEnum.FAILED);
        }
    }
}
