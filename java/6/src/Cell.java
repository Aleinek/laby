import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell {
    private final Rectangle view;
    private Animal occupant;
    private boolean paused;

    public Cell(int x, int y) {
        this.view = new Rectangle(GUI.CELL_SIZE, GUI.CELL_SIZE, Color.LIGHTGRAY);
        this.view.setStroke(Color.BLACK);
    }

    public synchronized Animal getOccupant() {
        return occupant;
    }

    public synchronized void setOccupant(Animal a) {
        this.occupant = a;
        if (a instanceof Hare) {
            Image hareImage = new Image("resources/Hare.png");
            view.setFill(new ImagePattern(hareImage));
            //view.setFill(Color.LIGHTGREEN);
        } else if (a instanceof Wolf) {
            Image wolfImage = new Image("resources/Wolf.png");
            view.setFill(new ImagePattern(wolfImage));  
            //view.setFill(Color.DARKRED); 
        }
    }

    public synchronized void clear() {
        this.occupant = null;
        view.setFill(Color.LIGHTGRAY);
    }

    public Rectangle getView() {
        return view;
    }

    public synchronized boolean isPaused() {
        return paused;
    }

    public synchronized void togglePause() {
        if (occupant != null) {
            paused = !paused;
            view.setOpacity(paused ? 0.3 : 1.0);
        }
    }
}