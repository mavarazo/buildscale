package com.mav.buildscale.controller;

import com.mav.buildscale.api.ReportsApi;
import com.mav.buildscale.api.model.ReportDto;
import com.mav.buildscale.api.model.ReportListDto;
import com.mav.buildscale.mapper.ReportMapper;
import com.mav.buildscale.model.Report;
import com.mav.buildscale.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportController implements ReportsApi {

    @Value("${buildscale.baseUrl}")
    private final String baseUrl;

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    @Override
    public ResponseEntity<ReportListDto> getReports(final Integer page, final Integer size, final List<String> sort) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        pageRequest.withSort(Sort.sort(Report.class).by(Report::getCreated).descending());

        final Page<Report> result = reportRepository.findAll(pageRequest);
        return ResponseEntity.ok(new ReportListDto()
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .elements(result.map(reportMapper::mapReportToReportDto).toList()));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> addReport(final ReportDto reportDto) {
        final Report report = reportRepository.save(reportMapper.mapReportDtoToReportDo(reportDto));

        final URI uri = ServletUriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/r/{reportId}")
                .buildAndExpand(report.getOid())
                .toUri();
        return ResponseEntity.created(uri).build();
    }
    
    @Override
    public ResponseEntity<ReportDto> getReportById(final String reportId) {
        return reportRepository.findById(reportId)
                .map(report -> ResponseEntity.ok(reportMapper.mapReportToReportDto(report)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
