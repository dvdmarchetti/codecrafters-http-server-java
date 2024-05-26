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
            final Socket clientSocket = serverSocket.accept();

            pool.submit(() -> {
                try {
                    HttpRequestReader reader = new HttpRequestReader(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
                    HttpRequest request = reader.read();
                    System.out.println(request);

                    HttpResponse response = new RouteMatcher().match(request);

                    HttpRequestWriter writer = new HttpRequestWriter(clientSocket.getOutputStream());
                    writer.write(response);
                } catch (IOException e) {
                    System.out.println("IOException during client handling: " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.out.println("IOException during server startup: " + e.getMessage());
        }
    }
}
