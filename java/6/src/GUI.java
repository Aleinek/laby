import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GUI extends Application {
    public static int width, height, hareCount, delayK;
    public static final int CELL_SIZE = 25;
    private static Board board;

    @Override
    public void start(Stage primaryStage) {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        width = Integer.parseInt(args[0]);
        height = Integer.parseInt(args[1]);
        hareCount = Integer.parseInt(args[2]);
        delayK = Integer.parseInt(args[3]);

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

        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Symulacja: Wilk i Zające");
        primaryStage.show();
    }

    public static void stopSimulation() {
        Platform.exit();
    }

    public static Board getBoard() {
        return board;
    }
}