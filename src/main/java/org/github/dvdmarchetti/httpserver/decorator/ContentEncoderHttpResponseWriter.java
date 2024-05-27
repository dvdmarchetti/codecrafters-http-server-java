package org.github.dvdmarchetti.httpserver.decorator;

import lombok.RequiredArgsConstructor;
import org.github.dvdmarchetti.httpserver.contract.HttpWriter;
import org.github.dvdmarchetti.httpserver.enumeration.HttpHeaders;
import org.github.dvdmarchetti.httpserver.model.HttpRequest;
import org.github.dvdmarchetti.httpserver.model.HttpResponse;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class ContentEncoderHttpResponseWriter implements HttpWriter {
    static private final String[] ACCEPTED_ENCODINGS = {
            "gzip"
    };

    private final HttpWriter writer;

    @Override
    public void write(HttpRequest request, HttpResponse response) throws IOException {
        String requestedEncoding = fetchRequestEncoding(request);
        if (requestedEncoding != null) {
            response.addHeader(HttpHeaders.CONTENT_ENCODING, requestedEncoding);
        }

        writer.write(request, response);
    }

    private String fetchRequestEncoding(HttpRequest request) {
        String encoding = request.getHeader(HttpHeaders.ACCEPT_ENCODING);
        if (encoding != null && Arrays.asList(ACCEPTED_ENCODINGS).contains(encoding)) {
            return encoding;
        }

        return null;
    }
}
