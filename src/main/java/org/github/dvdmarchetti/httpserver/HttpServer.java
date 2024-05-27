package org.github.dvdmarchetti.httpserver;

import org.github.dvdmarchetti.httpserver.model.HttpRequest;
import org.github.dvdmarchetti.httpserver.model.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class HttpServer {
    private final RouteMatcher routeMatcher;

    public HttpServer() {
        this.routeMatcher = new RouteMatcher();
    }

    public HttpServer(String path) {
        this.routeMatcher = new RouteMatcher(path);
    }

    public void accept(Socket clientSocket) throws IOException {
        HttpRequestReader reader = new HttpRequestReader(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
        HttpRequest request = reader.read();
        System.out.println(request);

        HttpResponse response = routeMatcher.match(request);

        HttpResponseWriter writer = new HttpResponseWriter(clientSocket.getOutputStream());
        writer.write(request, response);
    }
}
