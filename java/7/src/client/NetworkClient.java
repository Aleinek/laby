package client;

import common.*;

import java.io.*;
import java.net.Socket;

public class NetworkClient {

    private static final String HOST = "localhost"; // zmień na IP VPS jeśli trzeba
    private static final int PORT = 12345;

    public Response send(Request request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            return (Response) in.readObject();
        }
    }
}
