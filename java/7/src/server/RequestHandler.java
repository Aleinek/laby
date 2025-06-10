package server;

import common.*;

import java.io.*;
import java.net.Socket;

public class RequestHandler extends Thread {
    private final Socket socket;
    private final TreeManager manager;

    public RequestHandler(Socket socket, TreeManager manager) {
        this.socket = socket;
        this.manager = manager;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            while (true) {
                Request req = (Request) in.readObject();
                if (req == null || "exit".equalsIgnoreCase(req.command)) {
                    break;
                }

                Response res = handleRequest(req);
                out.writeObject(res);
                out.flush();
            }

        } catch (EOFException e) {
            System.out.println("Klient zakończył połączenie.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    private Response handleRequest(Request req) {
        TreeType type = req.type;
        String command = req.command.toLowerCase();
        String value = req.value;

        try {
            switch (type) {
                case INTEGER -> {
                    Integer intVal = (value != null) ? Integer.parseInt(value) : null;
                    return process(manager.getTypedTree(TreeType.INTEGER), command, intVal);
                }
                case DOUBLE -> {
                    Double doubleVal = (value != null) ? Double.parseDouble(value) : null;
                    return process(manager.getTypedTree(TreeType.DOUBLE), command, doubleVal);
                }
                case STRING -> {
                    return process(manager.getTypedTree(TreeType.STRING), command, value);
                }
                default -> {
                    return new Response("Unknown tree type", null);
                }
            }
        } catch (NumberFormatException e) {
            return new Response("Invalid number format: " + value, null);
        } catch (Exception e) {
            return new Response("Error processing request: " + e.getMessage(), null);
        }
    }

    private <T extends Comparable<T>> Response process(BinaryTree<T> tree, String command, T value) {
        switch (command) {
            case "insert" -> {
                tree.insert(value);
                return new Response("Inserted: " + value, tree.draw());
            }
            case "delete" -> {
                tree.delete(value);
                return new Response("Deleted: " + value, tree.draw());
            }
            case "search" -> {
                boolean found = tree.search(value);
                return new Response("Element " + (found ? "found" : "not found"), null);
            }
            case "draw" -> {
                return new Response("Tree structure:", tree.draw());
            }
            default -> {
                return new Response("Unknown command: " + command, null);
            }
        }
    }
}
