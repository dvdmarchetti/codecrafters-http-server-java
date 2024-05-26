import model.HttpRequest;
import model.HttpResponse;

public class RouteMatcher {
    public HttpResponse match(HttpRequest request) {
        return getResponse(request).build();
    }

    private HttpResponse.HttpResponseBuilder getResponse(HttpRequest request) {
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

        return HttpResponse.notFound();
    }
}
