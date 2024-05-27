package org.github.dvdmarchetti.httpserver;

import org.github.dvdmarchetti.httpserver.enumeration.HttpHeaders;
import org.github.dvdmarchetti.httpserver.enumeration.HttpMethod;
import org.github.dvdmarchetti.httpserver.model.HttpRequest;
import org.github.dvdmarchetti.httpserver.model.HttpResponse;

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
        HttpMethod method = request.getMethod();
        String path = request.getPath();

        if (HttpMethod.GET.equals(method) && "/".equals(path)) {
            return HttpResponse.ok();
        }

        if (HttpMethod.GET.equals(method) && path.startsWith("/echo")) {
            String param = path.replace("/echo/", "");
            return HttpResponse.ok().body(param);
        }

        if (HttpMethod.GET.equals(method) && path.startsWith("/user-agent")) {
            String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
            return HttpResponse.ok().body(userAgent);
        }

        if (HttpMethod.GET.equals(method) && path.startsWith("/files")) {
            String param = path.replace("/files/", "");

            Path targetFile = basePath.resolve(param);
            if (!targetFile.toFile().exists()) {
                return HttpResponse.notFound();
            }

            String contents = new String(Files.readAllBytes(targetFile));
            return HttpResponse.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                    .body(contents);
        }

        if (HttpMethod.POST.equals(method) && path.startsWith("/files")) {
            String param = path.replace("/files/", "");

            Path targetFile = basePath.resolve(param);
            if (targetFile.toFile().exists()) {
                return HttpResponse.internalServerError();
            }

            Files.write(targetFile, request.getBody().getBytes());
            return HttpResponse.created();
        }

        return HttpResponse.notFound();
    }
}
