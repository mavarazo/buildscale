package com.mav.buildscale.plugin.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mav.buildscale.plugin.internal.model.Report;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import java.net.URL;
import java.util.Optional;

public class ApiClient {

    private static final Logger LOGGER = Logging.getLogger(ApiClient.class);

    private final URL url;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public ApiClient(final URL url) {
        this(url, new OkHttpClient(), new ObjectMapper());
    }

    ApiClient(final URL url, final OkHttpClient client, final ObjectMapper objectMapper) {
        this.url = url;
        this.client = client;
        this.objectMapper = objectMapper;
    }

    public void postReport(final Report report) {
        try {
            final String json = objectMapper.writeValueAsString(report);
            final RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

            final Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            final Call call = client.newCall(request);
            try (final Response response = call.execute()) {
                if (!response.isSuccessful()) {
                    LOGGER.warn("Unable to publish build report. status: {}, response: {}, report: {}",
                            response.code(), Optional.ofNullable(response.body()).map(b -> b.toString()).orElse("empty"),
                            json);
                }
            }
        } catch (final Exception ex) {
            LOGGER.warn("Unable to publish build report", ex);
        }
    }
}
