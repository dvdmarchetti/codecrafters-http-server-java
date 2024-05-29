package org.github.dvdmarchetti.httpserver.decorator;

import lombok.RequiredArgsConstructor;
import org.github.dvdmarchetti.httpserver.contract.HttpWriter;
import org.github.dvdmarchetti.httpserver.enumeration.HttpHeaders;
import org.github.dvdmarchetti.httpserver.model.HttpRequest;
import org.github.dvdmarchetti.httpserver.model.HttpResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.GZIPOutputStream;

@RequiredArgsConstructor
public class GzipEncoderHttpResponseWriter implements HttpWriter {
    static private final String GZIP_ENCODING = "gzip";

    private final HttpWriter writer;

    @Override
    public void write(HttpRequest request, HttpResponse response) throws IOException {
        if (shouldCompressResponse(request)) {
            response = compress(response);
        }

        writer.write(request, response);
    }

    private boolean shouldCompressResponse(HttpRequest request) {
        List<String> encodings = request.getHeader(HttpHeaders.ACCEPT_ENCODING);
        if (encodings == null) {
            return false;
        }

        return encodings.contains(GZIP_ENCODING);
    }

    private HttpResponse compress(HttpResponse response) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream out = new GZIPOutputStream(baos);
        out.write(response.getBody());
        out.close();

        return HttpResponse.builder()
                .status(response.getStatus())
                .headers(response.getHeaders())
                .header(HttpHeaders.CONTENT_ENCODING, GZIP_ENCODING)
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(baos.size()))
                .body(baos.toByteArray())
                .build();
    }
}
