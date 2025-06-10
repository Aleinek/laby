package client;

import common.*;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Controller {

    @FXML private ComboBox<TreeType> treeTypeBox;
    @FXML private ChoiceBox<String> commandBox;
    @FXML private TextField valueField;
    @FXML private TextArea outputArea;
    @FXML private Canvas treeCanvas;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    @FXML
    public void initialize() {
        treeTypeBox.getItems().addAll(TreeType.INTEGER, TreeType.DOUBLE, TreeType.STRING);
        treeTypeBox.getSelectionModel().selectFirst();

        commandBox.getItems().addAll("insert", "delete", "search");
        commandBox.getSelectionModel().selectFirst();

        treeTypeBox.setOnAction(event -> onTreeTypeChange());

        try {
            socket = new Socket("localhost", 12345);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            outputArea.setText("Błąd połączenia z serwerem: " + e.getMessage());
        }

        onTreeTypeChange(); // automatyczne rysowanie po uruchomieniu
    }

    @FXML
    public void onExecute() {
        TreeType type = treeTypeBox.getValue();
        String command = commandBox.getValue();
        String value = valueField.getText().trim();

        if (command.matches("insert|delete|search") && value.isEmpty()) {
            outputArea.setText("Wprowadź wartość.");
            return;
        }

        try {
            out.writeObject(new Request(type, command, value));
            out.flush();
            Response res = (Response) in.readObject();

            outputArea.setText(res.message);
            if (res.treeData != null) {
                drawTree(res.treeData);
            } else if (res.treeOutput != null) {
                drawTreeText(res.treeOutput);
            } else {
                clearCanvas();
            }

        } catch (Exception e) {
            outputArea.setText("Błąd: " + e.getMessage());
        }
    }

    @FXML
    public void onDraw() {
        TreeType type = treeTypeBox.getValue();
        try {
            out.writeObject(new Request(type, "draw", null));
            out.flush();
            Response res = (Response) in.readObject();

            if (res.treeData != null) {
                outputArea.setText("Drzewo typu " + type + ":\n" + res.message);
                drawTree(res.treeData);
            } else if (res.treeOutput != null && !res.treeOutput.trim().isEmpty()) {
                outputArea.setText("Drzewo typu " + type + ":\n" + res.message);
                drawTreeText(res.treeOutput);
            } else {
                outputArea.setText("Drzewo typu " + type + " jest puste.");
                clearCanvas();
            }

        } catch (Exception e) {
            outputArea.setText("Błąd podczas rysowania: " + e.getMessage());
            clearCanvas();
        }
    }

    private void onTreeTypeChange() {
        onDraw();
    }

    // Pomocnicza klasa do przechowywania pozycji węzła
    private static class NodePos {
        TreeNodeDTO node;
        double x, y;
        NodePos(TreeNodeDTO node, double x, double y) {
            this.node = node; this.x = x; this.y = y;
        }
    }

    private int countLeaves(TreeNodeDTO node) {
        if (node == null) return 0;
        if (node.left == null && node.right == null) return 1;
        return countLeaves(node.left) + countLeaves(node.right);
    }

    private int getTreeDepth(TreeNodeDTO node) {
        if (node == null) return 0;
        return 1 + Math.max(getTreeDepth(node.left), getTreeDepth(node.right));
    }

    private void assignPositions(TreeNodeDTO node, double[] nextX, double y, double xStep, double yStep, java.util.Map<TreeNodeDTO, NodePos> posMap) {
        if (node == null) return;
        // Najpierw rekurencyjnie lewe i prawe dziecko
        if (node.left != null) assignPositions(node.left, nextX, y + yStep, xStep, yStep, posMap);
        if (node.right != null) assignPositions(node.right, nextX, y + yStep, xStep, yStep, posMap);
        double x;
        if (node.left == null && node.right == null) {
            x = nextX[0];
            nextX[0] += xStep;
        } else if (node.left != null && node.right != null) {
            x = (posMap.get(node.left).x + posMap.get(node.right).x) / 2;
        } else if (node.left != null) {
            x = posMap.get(node.left).x;
        } else {
            x = posMap.get(node.right).x;
        }
        posMap.put(node, new NodePos(node, x, y));
    }

    private void drawTree(TreeNodeDTO root) {
        GraphicsContext gc = treeCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, treeCanvas.getWidth(), treeCanvas.getHeight());
        if (root == null) return;
        int depth = getTreeDepth(root);
        int leaves = countLeaves(root);
        double canvasWidth = treeCanvas.getWidth();
        double canvasHeight = treeCanvas.getHeight();
        double yStep = canvasHeight / (depth + 1);
        double xStep = canvasWidth / (leaves + 1);
        java.util.Map<TreeNodeDTO, NodePos> posMap = new java.util.HashMap<>();
        assignPositions(root, new double[]{xStep}, yStep, xStep, yStep, posMap);
        // Rysuj krawędzie
        for (NodePos np : posMap.values()) {
            if (np.node.left != null) {
                NodePos left = posMap.get(np.node.left);
                gc.strokeLine(np.x, np.y, left.x, left.y);
            }
            if (np.node.right != null) {
                NodePos right = posMap.get(np.node.right);
                gc.strokeLine(np.x, np.y, right.x, right.y);
            }
        }
        // Rysuj węzły
        for (NodePos np : posMap.values()) {
            String valueStr = np.node.value != null ? np.node.value.toString() : "";
            gc.setFont(Font.font(14));
            javafx.scene.text.Text tempText = new javafx.scene.text.Text(valueStr);
            tempText.setFont(gc.getFont());
            double textWidth = tempText.getLayoutBounds().getWidth();
            double textHeight = tempText.getLayoutBounds().getHeight();
            double padding = 12;
            double ovalWidth = Math.max(36, textWidth + padding);
            double ovalHeight = Math.max(36, textHeight + padding);
            gc.setFill(Color.LIGHTBLUE);
            gc.fillOval(np.x - ovalWidth/2, np.y - ovalHeight/2, ovalWidth, ovalHeight);
            gc.setStroke(Color.BLACK);
            gc.strokeOval(np.x - ovalWidth/2, np.y - ovalHeight/2, ovalWidth, ovalHeight);
            gc.setFill(Color.BLACK);
            // Wycentrowanie tekstu
            double textX = np.x - textWidth/2;
            double textY = np.y + textHeight/4; // optyczne wyśrodkowanie
            gc.fillText(valueStr, textX, textY);
        }
    }

    // Stara metoda tekstowa (fallback)
    private void drawTreeText(String treeText) {
        GraphicsContext gc = treeCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, treeCanvas.getWidth(), treeCanvas.getHeight());
        String[] lines = treeText.split("\n");
        double x = 20, y = 30;
        gc.setFont(Font.font("Courier New", 14));
        gc.setFill(Color.BLACK);
        for (String line : lines) {
            gc.fillText(line, x, y);
            y += 18;
        }
    }

    private void clearCanvas() {
        GraphicsContext gc = treeCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, treeCanvas.getWidth(), treeCanvas.getHeight());
    }

    public void shutdown() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (Exception e) {
            System.err.println("Błąd przy zamykaniu połączenia: " + e.getMessage());
        }
    }
}
