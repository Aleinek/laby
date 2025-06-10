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
            if (res.treeOutput != null) {
                drawTree(res.treeOutput);
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

            if (res.treeOutput != null && !res.treeOutput.trim().isEmpty()) {
                outputArea.setText("Drzewo typu " + type + ":\n" + res.message);
                drawTree(res.treeOutput);
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

    private void drawTree(String treeText) {
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
