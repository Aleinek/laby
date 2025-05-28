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
        if (a instanceof Hare) view.setFill(Color.GREEN);
        else if (a instanceof Wolf) view.setFill(Color.RED);
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