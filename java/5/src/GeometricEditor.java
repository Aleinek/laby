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

    /** Enum for shape types */
    private enum ShapeType {
        CIRCLE, RECTANGLE, POLYGON
    }

    /** Canvas for drawing */
    private Canvas canvas;

    /** Graphics context for the canvas */
    private GraphicsContext gc;

    /** Shape color picker */
    private ColorPicker colorPicker;

    /** Selected shape type */
    private ShapeType selectedShape = ShapeType.CIRCLE;

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

    /** Indicates if a rectangle is being drawn */
    private boolean isDrawingRectangle = false;

    /** Temporary rectangle being drawn */
    private Shape tempRectangle = null;

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
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, this::onMouseReleased);
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
            selectedShape = ShapeType.CIRCLE;
            updateSelectedShapeText();
            resetPolygonDrawing();
        });
        rectangleItem.setOnAction(e -> {
            selectedShape = ShapeType.RECTANGLE;
            updateSelectedShapeText();
            resetPolygonDrawing();
        });
        polygonItem.setOnAction(e -> {
            selectedShape = ShapeType.POLYGON;
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
        if (activeShape != null) {
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
        if (isDrawingPolygon && selectedShape == ShapeType.POLYGON) {
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
                    case CIRCLE:
                        shape = new Shape(Shape.ShapeType.CIRCLE, lastMouseX, lastMouseY, 50, 50, colorPicker.getValue());
                        shapes.add(shape);
                        activeShape = shape;
                        redrawShapes();
                        break;
                    case RECTANGLE:
                        // Start drawing rectangle
                        isDrawingRectangle = true;
                        tempRectangle = new Shape(Shape.ShapeType.RECTANGLE, lastMouseX, lastMouseY, 1, 1, colorPicker.getValue());
                        activeShape = tempRectangle;
                        redrawShapes();
                        break;
                    default:
                        break;
                }
            } else {
                redrawShapes();
            }
        }
    }

    /**
     * Handles mouse drag events for moving shapes.
     *
     * @param e The mouse drag event
     */
    private void onMouseDragged(MouseEvent e) {
        if (isDrawingRectangle && tempRectangle != null) {
            double startX = lastMouseX;
            double startY = lastMouseY;
            double currX = e.getX();
            double currY = e.getY();
            double centerX = (startX + currX) / 2.0;
            double centerY = (startY + currY) / 2.0;
            double width = Math.abs(currX - startX);
            double height = Math.abs(currY - startY);

            tempRectangle.move(centerX - tempRectangle.getX(), centerY - tempRectangle.getY());
            // Set width and height directly (not via resize)
            try {
                java.lang.reflect.Field widthField = Shape.class.getDeclaredField("width");
                java.lang.reflect.Field heightField = Shape.class.getDeclaredField("height");
                widthField.setAccessible(true);
                heightField.setAccessible(true);
                widthField.set(tempRectangle, width);
                heightField.set(tempRectangle, height);
            } catch (Exception ex) {
                // Should not happen
            }
            redrawShapes();
        } else if (activeShape != null) {
            double dx = e.getX() - lastMouseX;
            double dy = e.getY() - lastMouseY;
            activeShape.move(dx, dy);
            lastMouseX = e.getX();
            lastMouseY = e.getY();
            redrawShapes();
        }
    }

    /**
     * Handles mouse release events for finishing rectangle drawing.
     *
     * @param e The mouse release event
     */
    private void onMouseReleased(MouseEvent e) {
        if (isDrawingRectangle && tempRectangle != null) {
            // Only add if width and height are reasonable
            if (tempRectangle.getWidth() > 5 && tempRectangle.getHeight() > 5) {
                shapes.add(tempRectangle);
            }
            activeShape = tempRectangle;
            isDrawingRectangle = false;
            tempRectangle = null;
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
            Shape polygon = new Shape(Shape.ShapeType.POLYGON, xPoints, yPoints, xPoints.length, colorPicker.getValue());
            shapes.add(polygon);
        }
        selectedShape = ShapeType.CIRCLE;
        updateSelectedShapeText();

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
                case Shape.ShapeType.CIRCLE:
                    gc.fillOval(shape.getX() - shape.getWidth() / 2.0, shape.getY() - shape.getHeight() / 2.0, shape.getWidth(), shape.getHeight());
                    break;
                case Shape.ShapeType.RECTANGLE:
                    gc.save();
                    gc.translate(shape.getX(), shape.getY());
                    gc.rotate(shape.getRotationAngle());
                    gc.fillRect(-shape.getWidth() / 2.0, -shape.getHeight() / 2.0, shape.getWidth(), shape.getHeight());
                    gc.restore();
                    break;
                case Shape.ShapeType.POLYGON:
                    gc.fillPolygon(shape.getXPoints(), shape.getYPoints(), shape.getPointCount());
                    break;
            }

            if (shape == activeShape) {
                gc.setStroke(Color.RED);
                gc.setLineWidth(2);
                switch (shape.getType()) {
                    case Shape.ShapeType.CIRCLE:
                        gc.strokeOval(shape.getX() - shape.getWidth() / 2.0, shape.getY() - shape.getHeight() / 2.0, shape.getWidth(), shape.getHeight());
                        break;
                    case Shape.ShapeType.RECTANGLE:
                        gc.save();
                        gc.translate(shape.getX(), shape.getY());
                        gc.rotate(shape.getRotationAngle());
                        gc.strokeRect(-shape.getWidth() / 2.0, -shape.getHeight() / 2.0, shape.getWidth(), shape.getHeight());
                        gc.restore();
                        break;
                    case Shape.ShapeType.POLYGON:
                        gc.strokePolygon(shape.getXPoints(), shape.getYPoints(), shape.getPointCount());
                        break;
                }
            }
        }

        // Draw the temp rectangle if drawing
        if (isDrawingRectangle && tempRectangle != null) {
            gc.setStroke(Color.GRAY);
            gc.setLineWidth(1);
            gc.strokeRect(
                tempRectangle.getX() - tempRectangle.getWidth() / 2.0,
                tempRectangle.getY() - tempRectangle.getHeight() / 2.0,
                tempRectangle.getWidth(),
                tempRectangle.getHeight()
            );
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
        selectedShapeText.setText("Selected Shape: " + selectedShape.name().charAt(0) + selectedShape.name().substring(1).toLowerCase());
    }

    /**
     * Displays the help dialog.
     */
    private void showHelpDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Geometric Editor");
        alert.setHeaderText("Simple Geometric Editor");
        alert.setContentText(
                "Author: Adam Kulwicki\n" +
                "Purpose: Create and modify geometric shapes.\n\n" +
                "Instructions:\n" +
                "1. Selecting Shapes:\n" +
                "   - Use the 'Shapes' menu to select the type of shape you want to draw: Circle, Rectangle, or Polygon.\n" +
                "   - For polygons, enter 'Polygon' mode and click on the canvas to add points. Click 'Finish Polygon' to complete.\n\n" +
                "2. Drawing Shapes:\n" +
                "   - Click on the canvas to draw a Circle or Rectangle at the clicked position.\n" +
                "   - Polygons require multiple clicks to define the vertices. Use the 'Finish Polygon' button to finalize the shape.\n\n" +
                "3. Modifying Shapes:\n" +
                "   - Click on a shape to select it.\n" +
                "   - Use the left and right arrow keys to rotate selected Rectangles or Polygons.\n" +
                "   - Drag the mouse to move the selected shape.\n" +
                "   - Scroll up or down to resize the selected shape.\n\n" +
                "4. Changing Color:\n" +
                "   - Select a shape, choose a color from the Color Picker, and right-click to change its color.\n\n" +
                "5. Deleting Shapes:\n" +
                "   - Right-click on a selected shape and choose 'Delete Shape' from the context menu.\n\n" +
                "6. Saving and Loading:\n" +
                "   - Use the 'File' menu to save or load shapes.\n" +
                "   - Saved files use the '.shapes' extension.\n\n" +
                "7. Additional Notes:\n" +
                "   - The canvas supports freeform drawing and modification, but ensure polygons have at least three points before finishing.\n"
            );
        alert.showAndWait();
    }

    /**
     * Handles scroll events for resizing shapes.
     *
     * @param event The scroll event
     */
    private void onScroll(ScrollEvent event) {
        if (activeShape != null) {
            double delta = event.getDeltaY();
            // Scale width and height by 10% per scroll step
            double scale = 1;
            if (delta > 0) {
                scale = 1.1;
            } else if (delta < 0) {
                scale = 0.9;
            }
            activeShape.resize(scale);
            redrawShapes();
        }
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