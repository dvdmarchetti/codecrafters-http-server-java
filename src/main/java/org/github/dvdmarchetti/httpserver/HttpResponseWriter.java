package org.github.dvdmarchetti.httpserver;

import org.github.dvdmarchetti.httpserver.contract.HttpWriter;
import org.github.dvdmarchetti.httpserver.enumeration.HttpHeaders;
import org.github.dvdmarchetti.httpserver.model.HttpRequest;
import org.github.dvdmarchetti.httpserver.model.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseWriter implements HttpWriter {
    static private final String HTTP_VERSION = "HTTP/1.1";
    static private final String EOL = "\r\n";

    private final OutputStream outputStream;

    public HttpResponseWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(HttpRequest request, HttpResponse response) throws IOException {
        writeLine(HTTP_VERSION + " " + response.getStatus().getCode() + " " + response.getStatus().getDescription());

        Map<String, String> headers = new HashMap<>(response.getHeaders());
        if (response.getBody() != null) {
            headers.putIfAbsent(HttpHeaders.CONTENT_TYPE, "text/plain");
        }

        for(Map.Entry<String, String> header : headers.entrySet()) {
            writeHeader(header.getKey(), header.getValue());
        }
        writeLine("");

        if (response.getBody() != null) {
            writeBody(response.getBody());
        }

        outputStream.flush();
    }

    private void writeHeader(String key, String value) throws IOException {
        writeLine(key + ": " + value);
    }

    private void writeLine(String line) throws IOException {
        outputStream.write((line + EOL).getBytes());
    }

    private void writeBody(byte[] bytes) throws IOException {
        outputStream.write(bytes);
    }
}
