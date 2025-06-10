package server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        TreeManager manager = new TreeManager();

        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server running on port 12345...");
            while (true) {
                Socket client = serverSocket.accept();
                new RequestHandler(client, manager).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
