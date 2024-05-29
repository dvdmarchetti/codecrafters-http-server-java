package org.github.dvdmarchetti.httpserver.model;

import lombok.*;
import org.github.dvdmarchetti.httpserver.enumeration.HttpHeaders;
import org.github.dvdmarchetti.httpserver.enumeration.HttpStatus;

import java.nio.charset.StandardCharsets;
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
    byte[] body;

    public HttpResponse withHeader(String key, String value) {
        Map<String, String> newHeaders = new HashMap<>(headers);
        newHeaders.put(key, value);

        return this.withHeaders(Collections.unmodifiableMap(newHeaders));
    }

    static public class HttpResponseBuilder {
        private byte[] body;

        public HttpResponseBuilder body(String body) {
            header(HttpHeaders.CONTENT_LENGTH, String.valueOf(body.length()));
            this.body = body.getBytes();

            return this;
        }

        public HttpResponseBuilder body(byte[] body) {
            header(HttpHeaders.CONTENT_LENGTH, String.valueOf(body.length));
            this.body = body;

            return this;
        }
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

    static public HttpResponseBuilder internalServerError() {
        return HttpResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
