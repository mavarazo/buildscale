package com.mav.buildscale.plugin.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mav.buildscale.plugin.internal.model.Report;
import lombok.SneakyThrows;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ApiClientTest {

    private ApiClient sut;

    private OkHttpClient client;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws MalformedURLException {
        client = mock(OkHttpClient.class);
        objectMapper = mock(ObjectMapper.class);
        sut = new ApiClient(new URL("http://localhost"), client, objectMapper);
    }

    @Test
    @SneakyThrows
    void unable_to_post_report() {
        // arrange
        when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);

        // act
        sut.postReport(new Report());

        // assert
        verifyNoInteractions(client);
    }

    @Test
    @SneakyThrows
    void post_report_unsuccessful() {
        // arrange
        when(objectMapper.writeValueAsString(any())).thenReturn("foo");

        final Call call = mock(Call.class);
        when(client.newCall(any())).thenReturn(call);
        final Response response = mock(Response.class);
        when(response.code()).thenReturn(404);
        when(call.execute()).thenReturn(response);

        // act
        sut.postReport(new Report());

        // assert
        verify(client).newCall(any());
        verify(call).execute();
    }

    @Test
    @SneakyThrows
    void post_report() {
        // arrange
        when(objectMapper.writeValueAsString(any())).thenReturn("foo");

        final Call call = mock(Call.class);
        when(client.newCall(any())).thenReturn(call);
        final Response response = mock(Response.class);
        when(response.code()).thenReturn(404);
        when(call.execute()).thenReturn(response);

        // act
        sut.postReport(new Report());

        // assert
        final ArgumentCaptor<Request> requestArgument = ArgumentCaptor.forClass(Request.class);
        verify(client).newCall(requestArgument.capture());

        Assertions.assertAll(() -> {
            Assertions.assertNotNull(requestArgument.getValue().url());
            assertEquals("http://localhost/", requestArgument.getValue().url().toString());
            Assertions.assertNotNull(requestArgument.getValue().body());
        });
    }
}