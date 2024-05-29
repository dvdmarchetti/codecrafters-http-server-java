package org.github.dvdmarchetti.httpserver.decorator;

import lombok.RequiredArgsConstructor;
import org.github.dvdmarchetti.httpserver.contract.HttpWriter;
import org.github.dvdmarchetti.httpserver.enumeration.HttpHeaders;
import org.github.dvdmarchetti.httpserver.model.HttpRequest;
import org.github.dvdmarchetti.httpserver.model.HttpResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ContentEncoderHttpResponseWriter implements HttpWriter {
    static private final Set<String> ACCEPTED_ENCODINGS = Set.of("gzip");

    private final HttpWriter writer;

    @Override
    public void write(HttpRequest request, HttpResponse response) throws IOException {
        List<String> requestedEncoding = extractRequestEncodings(request);
        if (requestedEncoding != null && !requestedEncoding.isEmpty()) {
            response = response.withHeader(HttpHeaders.CONTENT_ENCODING, requestedEncoding.getFirst());
        }

        writer.write(request, response);
    }

    private List<String> extractRequestEncodings(HttpRequest request) {
        List<String> encodings = request.getHeader(HttpHeaders.ACCEPT_ENCODING);
        if (encodings == null) {
            return List.of();
        }

        return encodings.stream()
                .filter(ACCEPTED_ENCODINGS::contains)
                .toList();
    }
}
