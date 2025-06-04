import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class GUI extends Application {
    public static int width, height, hareCount, delayK;
    public static final int CELL_SIZE = 25;
    private static Board board;

    private static final Logger logger = Logger.getLogger(GUI.class.getName());

    @Override
    public void start(Stage primaryStage) {
        showStartWindow(primaryStage);
    }

    private void showStartWindow(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        TextField widthField = new TextField();
        widthField.setPromptText("Enter width");

        TextField heightField = new TextField();
        heightField.setPromptText("Enter height");

        TextField hareCountField = new TextField();
        hareCountField.setPromptText("Enter hare count");

        TextField delayField = new TextField();
        delayField.setPromptText("Enter delay");

        Button startButton = new Button("Start Simulation");
        startButton.setOnAction(e -> {
            try {
                width = Integer.parseInt(widthField.getText());
                height = Integer.parseInt(heightField.getText());
                hareCount = Integer.parseInt(hareCountField.getText());
                delayK = Integer.parseInt(delayField.getText());

                if (hareCount > width * height - 1) {
                    throw new IllegalArgumentException("Hare count cannot exceed width * height - 1.");
                }

                // Proceed to simulation
                showSimulationWindow(primaryStage);
            } catch (NumberFormatException ex) {
                logger.warning("Invalid input: " + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                logger.warning("Invalid input: " + ex.getMessage());
            }
        });

        layout.getChildren().addAll(widthField, heightField, hareCountField, delayField, startButton);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation Setup");
        primaryStage.show();
    }

    private void showSimulationWindow(Stage primaryStage) {
        board = new Board(width, height);
        Simulation.init(board, hareCount, delayK);

        GridPane grid = new GridPane();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Rectangle rect = board.getCell(x, y).getView();
                int finalX = x, finalY = y;
                rect.setOnMouseClicked(e -> board.getCell(finalX, finalY).togglePause());
                grid.add(rect, x, y);
            }
        }

        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(grid);

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);

        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(e -> board.pauseSimulation());

        Button resumeButton = new Button("Resume");
        resumeButton.setOnAction(e -> board.resumeSimulation());

        buttonBox.getChildren().addAll(pauseButton, resumeButton);
        layout.getChildren().add(buttonBox);

        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Symulacja: Wilk i Zające");

        primaryStage.setOnCloseRequest(e -> {
            logger.info("Close request triggered.");

            long endTime = System.currentTimeMillis();
            long duration = (endTime - Simulation.startTime) / 1000; // Calculate duration in seconds

            logger.info("Simulation started at: " + Simulation.startTime);
            logger.info("Simulation ended at: " + endTime);
            logger.info("Simulation duration: " + duration + " seconds");

            VBox exitScreen = new VBox();
            exitScreen.setAlignment(Pos.CENTER);
            exitScreen.setSpacing(10);

            Text durationText = new Text("Simulation Duration: " + duration + " seconds");
            Text hareCountText = new Text("Hares Remaining: " + board.getHares().size());
            Text wolfText = new Text("Wolf Status: " + (board.getWolf() != null ? "Alive" : "Not Present"));

            exitScreen.getChildren().addAll(durationText, hareCountText, wolfText);

            Scene exitScene = new Scene(exitScreen, 400, 200);
            primaryStage.setScene(exitScene);

        });

        primaryStage.show();
    }

    public static void stopSimulation() {
        long endTime = System.currentTimeMillis();
        long duration = endTime - Simulation.startTime; // Calculate duration in milliseconds

        VBox exitScreen = new VBox();
        exitScreen.setAlignment(Pos.CENTER);
        exitScreen.setSpacing(10);

        Text durationText = new Text("Simulation Duration: " + duration + " milliseconds");
        Text hareCountText = new Text("Hares Remaining: " + board.getHares().size());
        Text wolfText = new Text("Wolf Status: " + (board.getWolf() != null ? "Alive" : "Not Present"));

        Button endButton = new Button("End");
        endButton.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });

        Button restartButton = new Button("Restart");
        restartButton.setOnAction(e -> {
            Platform.runLater(() -> {
                GUI guiInstance = new GUI();
                guiInstance.showStartWindow((Stage) exitScreen.getScene().getWindow()); // Navigate to the start screen
            });
        });

        exitScreen.getChildren().addAll(durationText, hareCountText, wolfText, endButton, restartButton);

        Scene exitScene = new Scene(exitScreen, 400, 200);
        Platform.runLater(() -> {
            Stage stage = (Stage) board.getCell(0, 0).getView().getScene().getWindow();
            stage.setScene(exitScene);
            stage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
            });
        });
    }

    public static Board getBoard() {
        return board;
    }
}