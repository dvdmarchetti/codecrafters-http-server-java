import java.io.IOException;
import java.io.OutputStream;
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
            System.out.println("Incoming connection");

            OutputStream output = clientSocket.getOutputStream();
            output.write("HTTP/1.1 200 OK\r\n".getBytes());
            output.write("\r\n".getBytes());
            output.flush();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
