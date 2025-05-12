/**
 * GeometricEditor is a Java graphical application for drawing and editing geometric shapes
 * such as circles, rectangles, and polygons. It supports saving/loading, changing color,
 * moving, scaling, and rotating shapes.
 *
 * Author: Adam Kulwicki
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
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

/**
 * The main class of the GeometricEditor application, which allows drawing and editing geometric shapes.
 */
public class GeometricEditor extends Application {

    /** Canvas for drawing */
    private Canvas canvas;

    /** Graphics context for the canvas */
    private GraphicsContext gc;

    /** Shape color picker */
    private ColorPicker colorPicker;

    /** Selected shape type */
    private String selectedShape = "Circle";

    /** Text displaying the selected shape type */
    private Text selectedShapeText;

    /** List of all shapes */
    private List<Shape> shapes = new ArrayList<>();

    /** Currently selected shape */
    private Shape activeShape = null;

    /** Last cursor position */
    private double lastMouseX, lastMouseY;

    /** Indicates whether a polygon is being drawn */
    private boolean isDrawingPolygon = false;

    /** X coordinates of the polygon */
    private List<Double> polygonXPoints = new ArrayList<>();

    /** Y coordinates of the polygon */
    private List<Double> polygonYPoints = new ArrayList<>();

    /** Button for finishing polygon drawing */
    private Button finishPolygonButton;

    /**
     * Default constructor for the GeometricEditor class.
     * Initializes the application by setting up necessary resources and configurations.
     */
    public GeometricEditor() {
        super();
    }

    /**
     * The main method of the application.
     *
     * @param args Input arguments for the application
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the user interface and event handling.
     *
     * @param primaryStage The main stage of the application
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Geometric Editor");
        BorderPane root = new BorderPane();
        canvas = new Canvas(1600, 900);
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        MenuBar menuBar = createMenuBar(primaryStage);
        VBox leftPanel = createLeftPanel();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onMousePressed);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onMouseDragged);
        canvas.addEventHandler(ScrollEvent.SCROLL, this::onScroll);

        root.setTop(menuBar);
        root.setLeft(leftPanel);
        root.setCenter(canvas);

        createContextMenuForShapes();

        Scene scene = new Scene(root, 1920, 1080);
        scene.setOnKeyPressed(this::onKeyPressed);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates the application's menu bar.
     *
     * @param primaryStage The main stage of the application
     * @return The application's menu bar
     */
    private MenuBar createMenuBar(Stage primaryStage) {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem loadItem = new MenuItem("Load");
        saveItem.setOnAction(e -> saveShapes(primaryStage));
        loadItem.setOnAction(e -> loadShapes(primaryStage));
        fileMenu.getItems().addAll(saveItem, loadItem);

        Menu shapeMenu = new Menu("Shapes");
        MenuItem circleItem = new MenuItem("Circle");
        MenuItem rectangleItem = new MenuItem("Rectangle");
        MenuItem polygonItem = new MenuItem("Polygon");

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
        shapeMenu.getItems().addAll(circleItem, rectangleItem, polygonItem);

        Menu helpMenu = new Menu("Help");
        MenuItem infoItem = new MenuItem("Info");
        infoItem.setOnAction(e -> showHelpDialog());
        helpMenu.getItems().add(infoItem);

        menuBar.getMenus().addAll(fileMenu, shapeMenu, helpMenu);
        return menuBar;
    }

    /**
     * Creates the tools panel on the left side.
     *
     * @return The tools panel
     */
    private VBox createLeftPanel() {
        VBox leftPanel = new VBox();
        leftPanel.setSpacing(10);

        Label colorLabel = new Label("Fill Color:");
        colorPicker = new ColorPicker(Color.BLACK);

        selectedShapeText = new Text("Selected Shape: Circle");
        updateSelectedShapeText();

        finishPolygonButton = new Button("Finish Polygon");
        finishPolygonButton.setDisable(true);
        finishPolygonButton.setOnAction(e -> finishPolygon());

        leftPanel.getChildren().addAll(colorLabel, colorPicker, selectedShapeText, finishPolygonButton);
        return leftPanel;
    }

    /**
     * Handles key press events.
     *
     * @param event The key press event
     */
    private void onKeyPressed(KeyEvent event) {
        if (activeShape != null && ("Rectangle".equals(activeShape.getType()) || "Polygon".equals(activeShape.getType()))) {
            switch (event.getCode()) {
                case LEFT:
                    activeShape.setRotationAngle(activeShape.getRotationAngle() - 5);
                    break;
                case RIGHT:
                    activeShape.setRotationAngle(activeShape.getRotationAngle() + 5);
                    break;
                default:
                    break;
            }
            redrawShapes();
        }
    }

    /**
     * Creates a context menu for shapes.
     */
    private void createContextMenuForShapes() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem changeColorItem = new MenuItem("Change Color");
        changeColorItem.setOnAction(e -> {
            if (activeShape != null) {
                activeShape.setColor(colorPicker.getValue());
                redrawShapes();
            }
        });

        MenuItem deleteShapeItem = new MenuItem("Delete Shape");
        deleteShapeItem.setOnAction(e -> {
            if (activeShape != null) {
                shapes.remove(activeShape);
                activeShape = null;
                redrawShapes();
            }
        });

        contextMenu.getItems().addAll(changeColorItem, deleteShapeItem);

        canvas.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                contextMenu.show(canvas, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
        });
    }

    /**
     * Handles mouse press events for drawing or selecting shapes.
     *
     * @param e The mouse press event
     */
    private void onMousePressed(MouseEvent e) {
        if (isDrawingPolygon && "Polygon".equals(selectedShape)) {
            polygonXPoints.add(e.getX());
            polygonYPoints.add(e.getY());
            activeShape = null;
            redrawShapes();
        } else {
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

    /**
     * Handles mouse drag events for moving shapes.
     *
     * @param e The mouse drag event
     */
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

    /**
     * Handles scroll events for resizing shapes.
     *
     * @param e The scroll event
     */
    private void onScroll(ScrollEvent e) {
        if (activeShape != null) {
            double scaleFactor = e.getDeltaY() < 0 ? 0.9 : 1.1;
            activeShape.resize(scaleFactor);
            redrawShapes();
        }
    }

    /**
     * Finalizes polygon drawing.
     */
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

    /**
     * Clears temporary data for polygon drawing.
     */
    private void resetPolygonDrawing() {
        isDrawingPolygon = false;
        polygonXPoints.clear();
        polygonYPoints.clear();
        if (finishPolygonButton != null) finishPolygonButton.setDisable(true);
    }

    /**
     * Draws all shapes on the canvas.
     */
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
                    gc.save();
                    gc.translate(shape.getX(), shape.getY());
                    gc.rotate(shape.getRotationAngle());
                    gc.fillRect(-shape.getWidth() / 2.0, -shape.getHeight() / 2.0, shape.getWidth(), shape.getHeight());
                    gc.restore();
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
                        gc.save();
                        gc.translate(shape.getX(), shape.getY());
                        gc.rotate(shape.getRotationAngle());
                        gc.strokeRect(-shape.getWidth() / 2.0, -shape.getHeight() / 2.0, shape.getWidth(), shape.getHeight());
                        gc.restore();
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

    /**
     * Updates the text showing the selected shape type.
     */
    private void updateSelectedShapeText() {
        selectedShapeText.setText("Selected Shape: " + selectedShape);
    }

    /**
     * Displays the help dialog.
     */
    private void showHelpDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Geometric Editor");
        alert.setHeaderText("Simple Geometric Editor");
        alert.setContentText("Author: Adam Kulwicki\nPurpose: Drawing and modifying geometric shapes.");
        alert.showAndWait();
    }

    /**
     * Saves shapes to a file.
     *
     * @param stage The main stage of the application
     */
    private void saveShapes(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Shapes");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Shape Files", "*.shapes"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            if (!file.getName().endsWith(".shapes")) {
                file = new File(file.getAbsolutePath() + ".shapes");
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(shapes);
                new Alert(Alert.AlertType.INFORMATION, "Shapes saved successfully!", ButtonType.OK).showAndWait();
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Error saving shapes: " + e.getMessage(), ButtonType.OK).showAndWait();
            }
        }
    }

    /**
     * Loads shapes from a file.
     *
     * @param stage The main stage of the application
     */
    @SuppressWarnings("unchecked")
    private void loadShapes(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Shapes");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Shape Files", "*.shapes"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                shapes = (List<Shape>) ois.readObject();
                activeShape = null;
                redrawShapes();
                new Alert(Alert.AlertType.INFORMATION, "Shapes loaded successfully!", ButtonType.OK).showAndWait();
            } catch (IOException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Error loading shapes: " + e.getMessage(), ButtonType.OK).showAndWait();
            }
        }
    }
}