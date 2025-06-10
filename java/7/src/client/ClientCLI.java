package client;

import common.*;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientCLI {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
            Scanner scanner = new Scanner(System.in);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("Witaj w kliencie CLI! Aby zakończyć, wpisz 'exit' w dowolnym momencie.");

            TreeType currentType = null;

            while (true) {
                // Wybór typu drzewa (jeśli jeszcze nie wybrano)
                if (currentType == null) {
                    System.out.println("Wybierz typ drzewa (INTEGER, DOUBLE, STRING): ");
                    String typeInput = scanner.nextLine().trim();
                    if (typeInput.equalsIgnoreCase("exit")) break;

                    try {
                        currentType = TreeType.valueOf(typeInput.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Nieprawidłowy typ. Spróbuj ponownie.");
                        continue;
                    }
                }

                // Wybór komendy
                System.out.println("Podaj komendę (insert, delete, search, draw, type, exit): ");
                String command = scanner.nextLine().trim().toLowerCase();
                if (command.equals("exit")) {
                    out.writeObject(new Request(currentType, "exit", null));
                    break;
                }

                if (command.equals("type")) {
                    currentType = null;
                    continue;
                }

                // Sprawdź poprawność komendy
                if (!command.matches("insert|delete|search|draw")) {
                    System.out.println("Nieprawidłowa komenda. Spróbuj ponownie.");
                    continue;
                }

                // Pobierz wartość tylko jeśli potrzebna
                String value = null;
                if (!command.equals("draw")) {
                    System.out.println("Podaj wartość:");
                    value = scanner.nextLine().trim();
                    if (value.equalsIgnoreCase("exit")) {
                        out.writeObject(new Request(currentType, "exit", null));
                        break;
                    }
                }

                // Wyślij zapytanie
                Request request = new Request(currentType, command, value);
                out.writeObject(request);

                // Odczytaj odpowiedź
                Response response = (Response) in.readObject();
                System.out.println("\n--- Odpowiedź ---");
                System.out.println(response.message);
                if (response.treeOutput != null) {
                    System.out.println(response.treeOutput);
                }
                System.out.println();
            }

            System.out.println("Zakończono.");

        } catch (Exception e) {
            System.out.println("Błąd połączenia z serwerem: " + e.getMessage());
            //e.printStackTrace();
        }
    }
}
