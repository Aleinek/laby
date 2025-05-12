import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
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

    // New fields for polygon drawing
    private boolean isDrawingPolygon = false; // Indicates polygon drawing mode
    private List<Double> polygonXPoints = new ArrayList<>();
    private List<Double> polygonYPoints = new ArrayList<>();
    private Button finishPolygonButton; // Button to finish polygon drawing

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Geometric Editor");

        // Main layout
        BorderPane root = new BorderPane();

        // Canvas for drawing
        canvas = new Canvas(1600, 900);
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
            resetPolygonDrawing();
        });
        rectangleItem.setOnAction(e -> {
            selectedShape = "Rectangle";
            updateSelectedShapeText();
            resetPolygonDrawing();
        });
        polygonItem.setOnAction(e -> {
            selectedShape = "Polygon";
            isDrawingPolygon = true;
            polygonXPoints.clear();
            polygonYPoints.clear();
            finishPolygonButton.setDisable(false);
            updateSelectedShapeText();
        });

        // Save and Load actions
        saveItem.setOnAction(e -> saveShapes(primaryStage));
        loadItem.setOnAction(e -> loadShapes(primaryStage));

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

        // Button to finish polygon drawing
        finishPolygonButton = new Button("Finish Polygon");
        finishPolygonButton.setDisable(true);
        finishPolygonButton.setOnAction(e -> finishPolygon());

        leftPanel.getChildren().addAll(colorLabel, colorPicker, selectedShapeText, finishPolygonButton);

        // Event handling for drawing, selecting, and resizing shapes
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onMousePressed);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onMouseDragged);
        canvas.addEventHandler(ScrollEvent.SCROLL, this::onScroll);

        // Add components to root layout
        root.setTop(menuBar);
        root.setLeft(leftPanel);
        root.setCenter(canvas);

        // Set up the scene
        Scene scene = new Scene(root, 1920, 1080);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void saveShapes(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Shapes");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Shape Files", "*.shapes"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            // Ensure the file has the .shapes extension
            if (!file.getName().endsWith(".shapes")) {
                file = new File(file.getAbsolutePath() + ".shapes");
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(shapes);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Shapes saved successfully!", ButtonType.OK);
                alert.showAndWait();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error saving shapes: " + e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void loadShapes(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Shapes");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Shape Files", "*.shapes"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                shapes = (List<Shape>) ois.readObject();
                activeShape = null; // Clear active shape
                redrawShapes();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Shapes loaded successfully!", ButtonType.OK);
                alert.showAndWait();
            } catch (IOException | ClassNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading shapes: " + e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    private void onMousePressed(MouseEvent e) {
        if (isDrawingPolygon && "Polygon".equals(selectedShape)) {
            // Add points to the polygon
            polygonXPoints.add(e.getX());
            polygonYPoints.add(e.getY());
            redrawShapes();
        } else {
            // Handle other shape drawing
            lastMouseX = e.getX();
            lastMouseY = e.getY();
            boolean shapeClicked = false;
            for (Shape shape : shapes) {
                if (shape.contains(lastMouseX, lastMouseY)) {
                    activeShape = shape;
                    shapeClicked = true;
                    break;
                }
            }

            if (!shapeClicked) {
                gc.setFill(colorPicker.getValue());
                Shape shape = null;

                switch (selectedShape) {
                    case "Circle":
                        shape = new Shape("Circle", lastMouseX, lastMouseY, 50, 50, colorPicker.getValue());
                        break;
                    case "Rectangle":
                        shape = new Shape("Rectangle", lastMouseX, lastMouseY, 80, 40, colorPicker.getValue());
                        break;
                }

                if (shape != null) {
                    shapes.add(shape);
                    activeShape = shape;
                }
            }

            redrawShapes();
        }
    }

    private void onMouseDragged(MouseEvent e) {
        if (activeShape != null) {
            double dx = e.getX() - lastMouseX;
            double dy = e.getY() - lastMouseY;

            activeShape.move(dx, dy);
            lastMouseX = e.getX();
            lastMouseY = e.getY();
            redrawShapes();
        }
    }

    private void onScroll(ScrollEvent e) {
        if (activeShape != null) {
            double scaleFactor = 1;
            if (e.getDeltaY() < 0) {
                scaleFactor = 0.9; // Zoom out
            } else if (e.getDeltaY() > 0) {
                scaleFactor = 1.1; // Zoom in
            }
            activeShape.resize(scaleFactor);
            redrawShapes();
        }
    }

    private void finishPolygon() {
        if (polygonXPoints.size() >= 3) {
            double[] xPoints = polygonXPoints.stream().mapToDouble(Double::doubleValue).toArray();
            double[] yPoints = polygonYPoints.stream().mapToDouble(Double::doubleValue).toArray();
            Shape polygon = new Shape("Polygon", xPoints, yPoints, xPoints.length, colorPicker.getValue());
            shapes.add(polygon);
        }
        resetPolygonDrawing();
        redrawShapes();
    }

    private void resetPolygonDrawing() {
        isDrawingPolygon = false;
        polygonXPoints.clear();
        polygonYPoints.clear();
        if (finishPolygonButton != null) {
            finishPolygonButton.setDisable(true);
        }
    }

    private void redrawShapes() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Shape shape : shapes) {
            gc.setFill(shape.getColor());
            switch (shape.getType()) {
                case "Circle":
                    gc.fillOval(shape.getX() - shape.getWidth() / 2.0, shape.getY() - shape.getHeight() / 2.0, shape.getWidth(), shape.getHeight());
                    break;
                case "Rectangle":
                    gc.fillRect(shape.getX() - shape.getWidth() / 2.0, shape.getY() - shape.getHeight() / 2.0, shape.getWidth(), shape.getHeight());
                    break;
                case "Polygon":
                    gc.fillPolygon(shape.getXPoints(), shape.getYPoints(), shape.getPointCount());
                    break;
            }

            if (shape == activeShape) {
                gc.setStroke(Color.RED);
                gc.setLineWidth(2);
                switch (shape.getType()) {
                    case "Circle":
                        gc.strokeOval(shape.getX() - shape.getWidth() / 2.0, shape.getY() - shape.getHeight() / 2.0, shape.getWidth(), shape.getHeight());
                        break;
                    case "Rectangle":
                        gc.strokeRect(shape.getX() - shape.getWidth() / 2.0, shape.getY() - shape.getHeight() / 2.0, shape.getWidth(), shape.getHeight());
                        break;
                    case "Polygon":
                        gc.strokePolygon(shape.getXPoints(), shape.getYPoints(), shape.getPointCount());
                        break;
                }
            }
        }

        if (isDrawingPolygon) {
            gc.setStroke(Color.GRAY);
            gc.setLineWidth(1);
            for (int i = 0; i < polygonXPoints.size() - 1; i++) {
                gc.strokeLine(polygonXPoints.get(i), polygonYPoints.get(i), polygonXPoints.get(i + 1), polygonYPoints.get(i + 1));
            }
        }
    }

    private void updateSelectedShapeText() {
        selectedShapeText.setText("Selected Shape: " + selectedShape);
    }
}