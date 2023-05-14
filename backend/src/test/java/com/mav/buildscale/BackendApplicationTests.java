package com.mav.buildscale;

import com.mav.buildscale.api.model.ReportDto;
import com.mav.buildscale.api.model.ReportListDto;
import com.mav.buildscale.api.model.TagDto;
import com.mav.buildscale.api.model.TaskDto;
import com.mav.buildscale.api.model.TestDto;
import com.mav.buildscale.api.model.TestFailureDto;
import com.mav.buildscale.repository.ReportRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BackendApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReportRepository reportRepository;

    @AfterEach
    void tearDown() {
        reportRepository.deleteAll();
    }

    @Nested
    class GetReportsTests {

        @Test
        @Sql(scripts = {"/db/test-data/report.sql"})
        void status200() {
            // act
            final ResponseEntity<ReportListDto> response = restTemplate.exchange("/v1/reports", HttpMethod.GET, HttpEntity.EMPTY, ReportListDto.class);

            // assert
            assertThat(response)
                    .isNotNull()
                    .returns(HttpStatus.OK, ResponseEntity::getStatusCode)
                    .satisfies(r -> assertThat(r.getBody())
                            .returns(1L, ReportListDto::getTotalElements)
                            .returns(1, ReportListDto::getTotalPages)
                            .satisfies(b -> assertThat(b.getElements())
                                    .anyMatch(reportDto -> reportDto.getId().equals("07066981-ca78-46a7-bcd2-7e99f3d6ac23")))
                    );
        }
    }

    @Nested
    class AddReportTests {

        @Test
        void status201() {
            // arrange
            final ReportDto reportDto = new ReportDto()
                    .project("buildscale-sample")
                    .hostname("localhost")
                    .durationInMillis(300.0)
                    .status(ReportDto.StatusEnum.FAILED)
                    .addTagsItem(new TagDto().key("gradle.version").value("8.0.2"))
                    .addTasksItem(new TaskDto()
                            .path(":bar:compileJava")
                            .startTime(1679600427153.0)
                            .endTime(1679600427195.0)
                            .status(TaskDto.StatusEnum.SKIPPED)
                            .messages(List.of("NO-SOURCE"))
                            .isIncremental(false)
                            .durationInMillis(42.0))
                    .addTasksItem(new TaskDto()
                            .path(":bar:test")
                            .startTime(1679600427318.0)
                            .endTime(1679600428082.0)
                            .status(TaskDto.StatusEnum.FAILED)
                            .durationInMillis(764.0))
                    .addTestsItem(new TestDto()
                            .name("bingo()")
                            .className("BarTest")
                            .durationInMillis(20.0)
                            .status(TestDto.StatusEnum.FAILED)
                            .addFailuresItem(new TestFailureDto()
                                    .message("expected: not <null>")
                                    .stacktrace("org.opentest4j.AssertionFailedError: expected: not <null>"))
                    );

            // act
            final ResponseEntity<Void> response = restTemplate.exchange("/v1/reports", HttpMethod.POST, new HttpEntity<>(reportDto), Void.class);

            // assert
            assertThat(response)
                    .isNotNull()
                    .returns(HttpStatus.CREATED, ResponseEntity::getStatusCode)
                    .satisfies(r -> assertThat(r.getHeaders())
                            .flatExtracting("Location")
                            .anyMatch(l -> l.toString().startsWith("https://buildscale.com/r/")));
        }
    }

    @Nested
    class GetReportByIdTests {

        @Test
        void status404() {
            // act
            final ResponseEntity<ReportDto> response = restTemplate.exchange("/v1/reports/unknown", HttpMethod.GET, HttpEntity.EMPTY, ReportDto.class);

            // assert
            assertThat(response)
                    .isNotNull()
                    .returns(HttpStatus.NOT_FOUND, ResponseEntity::getStatusCode);
        }

        @Test
        @Sql(scripts = {"/db/test-data/report.sql"})
        void status200() {
            // act
            final ResponseEntity<ReportDto> response = restTemplate.exchange("/v1/reports/07066981-ca78-46a7-bcd2-7e99f3d6ac23", HttpMethod.GET, HttpEntity.EMPTY, ReportDto.class);

            // assert
            assertThat(response)
                    .isNotNull()
                    .returns(HttpStatus.OK, ResponseEntity::getStatusCode)
                    .satisfies(r -> assertThat(r.getBody())
                            .returns("07066981-ca78-46a7-bcd2-7e99f3d6ac23", ReportDto::getId)
                            .returns("buildscale-sample", ReportDto::getProject)
                            .returns("localhost", ReportDto::getHostname)
                            .returns(300.0, ReportDto::getDurationInMillis)
                            .returns(ReportDto.StatusEnum.FAILED, ReportDto::getStatus)
                            .satisfies(report -> assertThat(report.getTags())
                                    .hasSize(1)
                                    .extracting(TagDto::getKey, TagDto::getValue)
                                    .contains(tuple("gradle.version", "8.0.2")))
                            .satisfies(report -> assertThat(report.getTasks())
                                    .hasSize(2)
                                    .extracting(TaskDto::getPath, TaskDto::getDurationInMillis, TaskDto::getStatus)
                                    .contains(
                                            tuple(":bar:compileJava", 42.0, TaskDto.StatusEnum.SKIPPED),
                                            tuple(":bar:compileTestJava", 764.0, TaskDto.StatusEnum.FAILED)))
                            .satisfies(report -> assertThat(report.getTests())
                                    .hasSize(1)
                                    .singleElement()
                                    .returns("bingo()", TestDto::getName)
                                    .returns("BarTest", TestDto::getClassName)
                                    .returns(20.0, TestDto::getDurationInMillis)
                                    .returns(TestDto.StatusEnum.FAILED, TestDto::getStatus)
                                    .satisfies(test -> assertThat(test.getFailures())
                                            .hasSize(1)
                                            .singleElement()
                                            .returns("expected: not <null>", TestFailureDto::getMessage)
                                            .returns("org.opentest4j.AssertionFailedError: expected: not <null>", TestFailureDto::getStacktrace))
                            )
                    );
        }
    }

    @Nested
    class GetTestByIdTests {

        @Test
        void status404() {
            // act
            final ResponseEntity<TestDto> response = restTemplate.exchange("/v1/tests/unknown", HttpMethod.GET, HttpEntity.EMPTY, TestDto.class);

            // assert
            assertThat(response)
                    .isNotNull()
                    .returns(HttpStatus.NOT_FOUND, ResponseEntity::getStatusCode);
        }

        @Test
        @Sql(scripts = {"/db/test-data/report.sql"})
        void status200() {
            // act
            final ResponseEntity<TestDto> response = restTemplate.exchange("/v1/tests/2aeaf54c-680a-48cb-a4e5-74584f0a63c8", HttpMethod.GET, HttpEntity.EMPTY, TestDto.class);

            // assert
            assertThat(response)
                    .isNotNull()
                    .returns(HttpStatus.OK, ResponseEntity::getStatusCode)
                    .satisfies(r -> assertThat(r.getBody())
                            .returns("bingo()", TestDto::getName)
                            .returns("BarTest", TestDto::getClassName)
                            .returns(20.0, TestDto::getDurationInMillis)
                            .returns(TestDto.StatusEnum.FAILED, TestDto::getStatus)
                            .satisfies(test -> assertThat(test.getFailures())
                                    .hasSize(1)
                                    .singleElement()
                                    .returns("expected: not <null>", TestFailureDto::getMessage)
                                    .returns("org.opentest4j.AssertionFailedError: expected: not <null>", TestFailureDto::getStacktrace))
                    );
        }
    }
}