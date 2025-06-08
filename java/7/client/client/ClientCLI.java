package client;

import common.*;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientCLI {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("Wybierz typ drzewa (INTEGER, DOUBLE, STRING): ");
            TreeType type = TreeType.valueOf(scanner.nextLine().trim().toUpperCase());

            System.out.println("Podaj komendę (insert, delete, search, draw): ");
            String command = scanner.nextLine().trim().toLowerCase();

            String value = null;
            if (!command.equals("draw")) {
                System.out.println("Podaj wartość: ");
                value = scanner.nextLine().trim();
            }

            Request request = new Request(type, command, value);
            out.writeObject(request);

            Response response = (Response) in.readObject();
            System.out.println("\n--- Odpowiedź ---");
            System.out.println(response.message);
            if (response.treeOutput != null) {
                System.out.println(response.treeOutput);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
