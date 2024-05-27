package org.github.dvdmarchetti.httpserver.contract;

import org.github.dvdmarchetti.httpserver.model.HttpRequest;
import org.github.dvdmarchetti.httpserver.model.HttpResponse;

import java.io.IOException;

public interface HttpWriter {
    void write(HttpRequest request, HttpResponse response) throws IOException;
}
