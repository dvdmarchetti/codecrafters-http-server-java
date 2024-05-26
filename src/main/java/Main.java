import model.HttpRequest;
import model.HttpResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(4221);
            serverSocket.setReuseAddress(true);
            clientSocket = serverSocket.accept();

            HttpRequestReader reader = new HttpRequestReader(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
            HttpRequest request = reader.read();
            System.out.println(request);

            HttpResponse response = new RouteMatcher().match(request);

            HttpRequestWriter writer = new HttpRequestWriter(clientSocket.getOutputStream());
            writer.write(response);
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
