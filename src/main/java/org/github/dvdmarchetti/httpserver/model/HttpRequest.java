package org.github.dvdmarchetti.httpserver.model;

import org.github.dvdmarchetti.httpserver.enumeration.HttpMethod;
import lombok.Builder;
import lombok.Singular;
import lombok.ToString;
import lombok.Value;

import java.util.Map;

@Value
@Builder
@ToString
public class HttpRequest {
    HttpMethod method;
    String path;
    @Singular
    Map<String, String> headers;
    String body;

    public String getHeader(String key) {
        return headers.get(key);
    }
}
