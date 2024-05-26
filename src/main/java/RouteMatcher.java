import model.HttpRequest;
import model.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RouteMatcher {
    private final Path basePath;

    public RouteMatcher() {
        this.basePath = null;
    }

    public RouteMatcher(String basePath) {
        this.basePath = Path.of(basePath);
    }

    public HttpResponse match(HttpRequest request) throws IOException {
        return getResponse(request).build();
    }

    private HttpResponse.HttpResponseBuilder getResponse(HttpRequest request) throws IOException {
        String path = request.getPath();

        if ("/".equals(path)) {
            return HttpResponse.ok();
        }

        if (path.startsWith("/echo")) {
            String param = path.replace("/echo/", "");
            return HttpResponse.ok().body(param);
        }

        if (path.startsWith("/user-agent")) {
            String userAgent = request.getHeader("User-Agent");
            return HttpResponse.ok().body(userAgent);
        }

        if (path.startsWith("/files")) {
            String param = path.replace("/files/", "");

            Path targetFile = basePath.resolve(param);
            if (!targetFile.toFile().exists()) {
                return HttpResponse.notFound();
            }

            String contents = new String(Files.readAllBytes(targetFile));
            return HttpResponse.ok()
                    .header("Content-Type", "application/octet-stream")
                    .body(contents);
        }

        return HttpResponse.notFound();
    }
}
