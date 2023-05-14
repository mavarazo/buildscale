package com.mav.buildscale.mapper;

import com.mav.buildscale.api.model.ReportDto;
import com.mav.buildscale.model.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {TagMapper.class, TaskMapper.class, TestMapper.class})
public interface ReportMapper {

    Report mapReportDtoToReportDo(final ReportDto reportDto);

    @Mapping(target = "id", source = "oid")
    ReportDto mapReportToReportDto(final Report report);
}
