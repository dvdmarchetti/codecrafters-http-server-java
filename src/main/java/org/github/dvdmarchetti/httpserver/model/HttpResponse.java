package org.github.dvdmarchetti.httpserver.model;

import lombok.*;
import org.github.dvdmarchetti.httpserver.enumeration.HttpStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@With
@Value
@Builder
@ToString
public class HttpResponse {
    HttpStatus status;
    @Singular
    Map<String, String> headers;
    String body;

    public HttpResponse withHeader(String key, String value) {
        Map<String, String> newHeaders = new HashMap<>(headers);
        newHeaders.put(key, value);

        return this.withHeaders(Collections.unmodifiableMap(newHeaders));
    }

    static public HttpResponseBuilder ok() {
        return HttpResponse.builder()
                .status(HttpStatus.OK);
    }

    static public HttpResponseBuilder created() {
        return HttpResponse.builder()
                .status(HttpStatus.CREATED);
    }

    static public HttpResponseBuilder notFound() {
        return HttpResponse.builder()
                .status(HttpStatus.NOT_FOUND);
    }

    public static HttpResponseBuilder internalServerError() {
        return HttpResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
