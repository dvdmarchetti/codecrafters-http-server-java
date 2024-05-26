package model;

import lombok.Builder;
import lombok.Singular;
import lombok.ToString;
import lombok.Value;

import java.util.Map;

@Value
@Builder
@ToString
public class HttpRequest {
    String method;
    String path;
    @Singular
    Map<String, String> headers;
    String body;

    public String getHeader(String key) {
        return headers.get(key.toLowerCase());
    }
}
