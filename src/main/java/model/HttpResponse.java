package model;

import lombok.Builder;
import lombok.Singular;
import lombok.ToString;
import lombok.Value;

import java.util.Map;

@Value
@Builder
@ToString
public class HttpResponse {
    Integer code;
    String status;
    @Singular
    Map<String, String> headers;
    String body;

    static public HttpResponseBuilder ok() {
        return HttpResponse.builder()
                .code(200)
                .status("OK");
    }

    static public HttpResponseBuilder notFound() {
        return HttpResponse.builder()
                .code(404)
                .status("Not Found");
    }
}
