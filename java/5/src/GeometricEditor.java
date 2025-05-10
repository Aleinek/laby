import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GeometricEditor extends Application {

    private Canvas canvas;
    private GraphicsContext gc;
    private ColorPicker colorPicker;
    private String selectedShape = "Circle"; // Default shape
    private Text selectedShapeText; // Text to display the selected shape
    private List<Shape> shapes = new ArrayList<>(); // List to store shapes
    private Shape activeShape = null; // Currently selected shape for modification
    private double lastMouseX, lastMouseY; // Variables to track mouse movement

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Geometric Editor");

        // Main layout
        BorderPane root = new BorderPane();

        // Canvas for drawing
        canvas = new Canvas(1600, 900); // Slightly smaller for 1920x1080 resolution
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Top menu
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem loadItem = new MenuItem("Load");
        fileMenu.getItems().addAll(saveItem, loadItem);

        Menu shapeMenu = new Menu("Shapes");
        MenuItem circleItem = new MenuItem("Circle");
        MenuItem rectangleItem = new MenuItem("Rectangle");
        MenuItem polygonItem = new MenuItem("Polygon");
        shapeMenu.getItems().addAll(circleItem, rectangleItem, polygonItem);

        Menu helpMenu = new Menu("Help");
        MenuItem infoItem = new MenuItem("Info");
        helpMenu.getItems().add(infoItem);

        menuBar.getMenus().addAll(fileMenu, shapeMenu, helpMenu);

        // Shape selection actions
        circleItem.setOnAction(e -> {
            selectedShape = "Circle";
            updateSelectedShapeText();
        });
        rectangleItem.setOnAction(e -> {
            selectedShape = "Rectangle";
            updateSelectedShapeText();
        });
        polygonItem.setOnAction(e -> {
            selectedShape = "Polygon";
            updateSelectedShapeText();
        });

        // Info dialog
        infoItem.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About Geometric Editor");
            alert.setHeaderText("Simple Geometric Editor");
            alert.setContentText("Author: Aleinek\nPurpose: Create and modify geometric shapes.\nResolution: Optimized for 1920x1080.");
            alert.showAndWait();
        });

        // Left panel with controls
        VBox leftPanel = new VBox();
        leftPanel.setSpacing(10);

        Label colorLabel = new Label("Fill Color:");
        colorPicker = new ColorPicker(Color.BLACK);

        // Text to display the selected shape
        selectedShapeText = new Text("Selected Shape: Circle");
        updateSelectedShapeText();

        leftPanel.getChildren().addAll(colorLabel, colorPicker, selectedShapeText);

        // Event handling for drawing and modifying shapes
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onMousePressed);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onMouseDragged);
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, this::onMouseReleased);

        // Add components to root layout
        root.setTop(menuBar);
        root.setLeft(leftPanel);
        root.setCenter(canvas);

        // Set up the scene
        Scene scene = new Scene(root, 1920, 1080);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void onMousePressed(MouseEvent e) {
        lastMouseX = e.getX();
        lastMouseY = e.getY();

        // Check if a shape is clicked
        for (Shape shape : shapes) {
            if (shape.contains(lastMouseX, lastMouseY)) {
                activeShape = shape;
                return;
            }
        }

        // If no shape is clicked, create a new shape
        gc.setFill(colorPicker.getValue());
        Shape shape = null;

        switch (selectedShape) {
            case "Circle":
                shape = new Shape("Circle", lastMouseX, lastMouseY, 50, 50, colorPicker.getValue());
                gc.fillOval(lastMouseX - 25, lastMouseY - 25, 50, 50);
                break;
            case "Rectangle":
                shape = new Shape("Rectangle", lastMouseX, lastMouseY, 80, 40, colorPicker.getValue());
                gc.fillRect(lastMouseX - 40, lastMouseY - 20, 80, 40);
                break;
            case "Polygon":
                double[] xPoints = {lastMouseX, lastMouseX + 20, lastMouseX - 20};
                double[] yPoints = {lastMouseY, lastMouseY + 30, lastMouseY + 30};
                shape = new Shape("Polygon", xPoints, yPoints, 3, colorPicker.getValue());
                gc.fillPolygon(xPoints, yPoints, 3);
                break;
        }

        if (shape != null) {
            shapes.add(shape);
            activeShape = shape;
        }
    }

    private void onMouseDragged(MouseEvent e) {
        if (activeShape != null) {
            double dx = e.getX() - lastMouseX;
            double dy = e.getY() - lastMouseY;
            activeShape.move(dx, dy);
            redrawShapes();
            lastMouseX = e.getX();
            lastMouseY = e.getY();
        }
    }

    private void onMouseReleased(MouseEvent e) {
        activeShape = null;
    }

    private void redrawShapes() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (Shape shape : shapes) {
            gc.setFill(shape.getColor());
            switch (shape.getType()) {
                case "Circle":
                    gc.fillOval(shape.getX() - 25, shape.getY() - 25, shape.getWidth(), shape.getHeight());
                    break;
                case "Rectangle":
                    gc.fillRect(shape.getX() - 40, shape.getY() - 20, shape.getWidth(), shape.getHeight());
                    break;
                case "Polygon":
                    gc.fillPolygon(shape.getXPoints(), shape.getYPoints(), shape.getPointCount());
                    break;
            }
        }
    }

    private void updateSelectedShapeText() {
        selectedShapeText.setText("Selected Shape: " + selectedShape);
    }
}
    