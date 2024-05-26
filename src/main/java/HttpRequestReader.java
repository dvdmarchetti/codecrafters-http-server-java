import exception.InvalidHttpRequestException;
import model.HttpRequest;

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
                .method(requestLineMatcher.group(1))
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
            headers.put(header[0].toLowerCase(), header[1]);
        }

        return headers;
    }

    private String parseRequestBody(Map<String, String> headers) throws IOException {
        if (!headers.containsKey("Content-Length")) {
            return null;
        }

        int maxLength = Integer.parseInt(headers.get("Content-Length"));
        char[] messageByte = new char[maxLength];

        boolean end = false;
        StringBuilder body = new StringBuilder(maxLength);
        int totalBytesRead = 0;
        while (!end) {
            int currentBytesRead = reader.read(messageByte);
            totalBytesRead = currentBytesRead + totalBytesRead;
            if (totalBytesRead <= maxLength) {
                body.append(new String(messageByte, 0, currentBytesRead));
            } else {
                body.append(new String(messageByte, 0, maxLength - totalBytesRead + currentBytesRead));
            }
            if (body.length() >= maxLength) {
                end = true;
            }
        }

        return body.toString();
    }
}
