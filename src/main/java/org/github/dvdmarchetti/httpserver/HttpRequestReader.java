package org.github.dvdmarchetti.httpserver;

import org.github.dvdmarchetti.httpserver.enumeration.HttpHeaders;
import org.github.dvdmarchetti.httpserver.enumeration.HttpMethod;
import org.github.dvdmarchetti.httpserver.exception.InvalidHttpRequestException;
import org.github.dvdmarchetti.httpserver.model.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HttpRequestReader {
    static private final Pattern REQUEST_LINE_REGEX = Pattern.compile("(HEAD|GET|POST|PUT|PATCH|DELETE|OPTIONS) (\\S+) HTTP/1.1");

    private final BufferedReader reader;

    public HttpRequestReader(BufferedReader reader) {
        this.reader = reader;
    }

    public HttpRequest read() throws IOException {
        final Matcher requestLineMatcher = REQUEST_LINE_REGEX.matcher(reader.readLine());
        if (! requestLineMatcher.matches()) {
            throw new InvalidHttpRequestException();
        }

        final Map<String, List<String>> headers = parseRequestHeaders();
        final String requestBody = parseRequestBody(headers);

        return HttpRequest.builder()
                .method(HttpMethod.valueOf(requestLineMatcher.group(1)))
                .path(requestLineMatcher.group(2))
                .headers(headers)
                .body(requestBody)
                .build();
    }

    private Map<String, List<String>> parseRequestHeaders() throws IOException {
        final Map<String, List<String>> headers = new HashMap<>();

        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] header = line.split(": ");

            List<String> values = Arrays.stream(header[1].split(",")).map(String::strip).toList();
            headers.put(header[0], values);
        }

        return headers;
    }

    private String parseRequestBody(Map<String, List<String>> headers) throws IOException {
        if (!headers.containsKey(HttpHeaders.CONTENT_LENGTH)) {
            return null;
        }

        int maxLength = Integer.parseInt(headers.get(HttpHeaders.CONTENT_LENGTH).getFirst());
        StringBuilder body = new StringBuilder(maxLength);
        while (reader.ready()) {
            body.append((char) reader.read());
        }

        return body.toString();
    }
}
