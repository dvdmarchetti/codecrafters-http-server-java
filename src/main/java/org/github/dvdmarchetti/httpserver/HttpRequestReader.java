package org.github.dvdmarchetti.httpserver;

import org.github.dvdmarchetti.httpserver.enumeration.HttpHeaders;
import org.github.dvdmarchetti.httpserver.enumeration.HttpMethod;
import org.github.dvdmarchetti.httpserver.exception.InvalidHttpRequestException;
import org.github.dvdmarchetti.httpserver.model.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        final Map<String, String> headers = parseRequestHeaders();
        final String requestBody = parseRequestBody(headers);

        return HttpRequest.builder()
                .method(HttpMethod.valueOf(requestLineMatcher.group(1)))
                .path(requestLineMatcher.group(2))
                .headers(headers)
                .body(requestBody)
                .build();
    }

    private Map<String, String> parseRequestHeaders() throws IOException {
        final Map<String, String> headers = new HashMap<>();

        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] header = line.split(": ");
            headers.put(header[0], header[1]);
        }

        return headers;
    }

    private String parseRequestBody(Map<String, String> headers) throws IOException {
        if (!headers.containsKey(HttpHeaders.CONTENT_LENGTH)) {
            return null;
        }

        int maxLength = Integer.parseInt(headers.get(HttpHeaders.CONTENT_LENGTH));
        StringBuilder body = new StringBuilder(maxLength);
        while (reader.ready()) {
            body.append((char) reader.read());
        }

        return body.toString();
    }
}
