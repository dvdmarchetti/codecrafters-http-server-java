package org.github.dvdmarchetti.httpserver.model;

import lombok.*;
import org.github.dvdmarchetti.httpserver.enumeration.HttpStatus;

import java.util.Map;

@Value
@Builder
@ToString
public class HttpResponse {
    HttpStatus status;
    @Singular
    Map<String, String> headers;
    String body;

    public void addHeader(String key, String value) {
        headers.put(key, value);
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
