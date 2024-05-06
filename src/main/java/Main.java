import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static Pattern PATH_REGEX = Pattern.compile("(GET|POST) (\\S+) HTTP/1.1");
    private static String NOT_FOUND = "HTTP/1.1 404 Not found\r\n\r\n";

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(4221);
            serverSocket.setReuseAddress(true);
            clientSocket = serverSocket.accept();

            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream output = clientSocket.getOutputStream();

            String requestLine = input.readLine();
            System.out.printf("Request: %s", requestLine);

            Matcher matcher = PATH_REGEX.matcher(requestLine);
            if (! matcher.matches()) {
                output.write(NOT_FOUND.getBytes());
            }

            String path = matcher.group(2);
            if ("/".equals(path)) {
                output.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
            } else {
                output.write(NOT_FOUND.getBytes());
            }
            output.flush();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
