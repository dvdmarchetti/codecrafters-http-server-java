import model.HttpRequest;
import model.HttpResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    public static void main(String[] args) {
        try (
                ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
                ServerSocket serverSocket = new ServerSocket(4221)
        ) {
            serverSocket.setReuseAddress(true);

            final HttpServer httpServer;
            if (args.length >= 2 && args[1] != null) {
                httpServer = new HttpServer(args[1]);
            } else {
                httpServer = new HttpServer();
            }

            while (true) {
                final Socket clientSocket = serverSocket.accept();

                pool.submit(() -> {
                    try {
                        httpServer.accept(clientSocket);
                    } catch (IOException e) {
                        System.out.println("IOException during client handling: " + e.getMessage());
                    }
                });
            }
        } catch (IOException e) {
            System.out.println("IOException during server startup: " + e.getMessage());
        }
    }
}
