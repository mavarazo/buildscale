package com.mav.buildscale.controller;

import com.mav.buildscale.api.ReportsApi;
import com.mav.buildscale.api.model.AddReport201Response;
import com.mav.buildscale.api.model.ReportDto;
import com.mav.buildscale.mapper.ReportMapper;
import com.mav.buildscale.model.Report;
import com.mav.buildscale.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportController implements ReportsApi {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    @Override
    public ResponseEntity<List<ReportDto>> getReports() {
        return ReportsApi.super.getReports();
    }

    @Override
    @Transactional
    public ResponseEntity<AddReport201Response> addReport(final ReportDto reportDto) {
        final Report report = reportRepository.save(reportMapper.mapReportDtoToReportDo(reportDto));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new AddReport201Response().id(report.getOid()));
    }

    @Override
    public ResponseEntity<ReportDto> getReportById(final String reportId) {
        return ReportsApi.super.getReportById(reportId);
    }
}
