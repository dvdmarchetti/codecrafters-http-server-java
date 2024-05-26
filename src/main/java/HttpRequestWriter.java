import model.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestWriter {
    static private final String HTTP_VERSION = "HTTP/1.1";
    static private final String EOL = "\r\n";

    private final OutputStream outputStream;

    public HttpRequestWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(HttpResponse response) throws IOException {
        writeLine(HTTP_VERSION + " " + response.getCode() + " " + response.getStatus());
        writeLine("");

        Map<String, String> headers = new HashMap<>(response.getHeaders());
        if (response.getBody() != null) {
            headers.putIfAbsent("content-type", "text/plain");
            headers.putIfAbsent("content-length", String.valueOf(response.getBody().length()));
        }

        for(Map.Entry<String, String> header : response.getHeaders().entrySet()) {
            writeHeader(header.getKey(), header.getValue());
        }
        writeLine("");

        if (response.getBody() != null) {
            writeBody(response.getBody().getBytes());
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
