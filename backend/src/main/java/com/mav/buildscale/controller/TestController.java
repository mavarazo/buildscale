package com.mav.buildscale.controller;

import com.mav.buildscale.api.TestsApi;
import com.mav.buildscale.api.model.TestDto;
import com.mav.buildscale.mapper.TestMapper;
import com.mav.buildscale.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TestController implements TestsApi {

    private final TestRepository testRepository;
    private final TestMapper testMapper;

    @Override
    public ResponseEntity<TestDto> getTestById(final String testId) {
        return testRepository.findById(testId)
                .map(test -> ResponseEntity.ok(testMapper.mapTestToTestDto(test)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
