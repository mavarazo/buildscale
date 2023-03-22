package com.mav.buildscale;

import com.mav.buildscale.api.model.AddReport201Response;
import com.mav.buildscale.api.model.ReportDto;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BackendApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    class GetReportsTest {

        @Test
        @Sql(scripts = {"/db/test-data/report.sql"})
        void status200() {
            // arrange

            // act
            final ResponseEntity<ReportDto[]> response = restTemplate.exchange("/reports", HttpMethod.GET, HttpEntity.EMPTY, ReportDto[].class);

            // assert
            assertThat(response)
                    .isNotNull()
                    .returns(HttpStatus.OK, ResponseEntity::getStatusCode);
        }
    }

    @Nested
    class AddReportTest {

        @Test
        void status201() {
            // arrange
            final ReportDto reportDto = new ReportDto()
                    .project("buildscale-sample")
                    .hostname("localhost")
                    .durationInMillis(300.0);

            // act
            final ResponseEntity<AddReport201Response> response = restTemplate.exchange("/reports", HttpMethod.POST, new HttpEntity<>(reportDto), AddReport201Response.class);

            // assert
            assertThat(response)
                    .isNotNull()
                    .returns(HttpStatus.CREATED, ResponseEntity::getStatusCode)
                    .extracting(HttpEntity::getBody)
                    .doesNotReturn(null, AddReport201Response::getId);
        }
    }
}