import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Klasa reprezentująca pojedynczą komórkę na planszy.
 * Przechowuje informacje o zwierzęciu i stanie zatrzymania.
 */
public class Cell {
    /** Graficzna reprezentacja komórki. */
    private final Rectangle view;
    /** Zwierzę znajdujące się w komórce. */
    private Animal occupant;
    /** Flaga określająca, czy zwierzę w komórce jest zatrzymane. */
    private boolean paused;

    /**
     * Tworzy komórkę o podanych współrzędnych.
     * 
     * @param x pozycja x (nieużywana wizualnie, ale informacyjnie)
     * @param y pozycja y (nieużywana wizualnie, ale informacyjnie)
     */
    public Cell(int x, int y) {
        this.view = new Rectangle(GUI.CELL_SIZE, GUI.CELL_SIZE, Color.LIGHTGRAY);
        this.view.setStroke(Color.BLACK);
    }

    /**
     * Zwraca zwierzę zajmujące komórkę.
     * 
     * @return zwierzę lub null, jeśli komórka jest pusta
     */
    public synchronized Animal getOccupant() {
        return occupant;
    }

    /**
     * Ustawia zwierzę w komórce i aktualizuje wygląd.
     * 
     * @param a zwierzę do umieszczenia
     */
    public synchronized void setOccupant(Animal a) {
        this.occupant = a;
        if (a instanceof Hare) {
            // W rzeczywistości trzeba by załadować obrazek, ale to tylko przykład
            Image hareImage = new Image("resources/Hare.png");
            view.setFill(new ImagePattern(hareImage));
            //view.setFill(Color.LIGHTGREEN);
        } else if (a instanceof Wolf) {
            Image wolfImage = new Image("resources/Wolf.png");
            view.setFill(new ImagePattern(wolfImage));  
            //view.setFill(Color.DARKRED); 
        }
    }

    /**
     * Czyści komórkę (usuwa zwierzę i resetuje wygląd).
     */
    public synchronized void clear() {
        this.occupant = null;
        view.setFill(Color.LIGHTGRAY);
        view.setOpacity(1.0); // Reset opacity
    }

    /**
     * Zwraca graficzną reprezentację komórki.
     * 
     * @return prostokąt reprezentujący komórkę
     */
    public Rectangle getView() {
        return view;
    }

    /**
     * Sprawdza, czy zwierzę w komórce jest zatrzymane.
     * 
     * @return true, jeśli zwierzę jest zatrzymane; false w przeciwnym razie
     */
    public synchronized boolean isPaused() {
        return paused;
    }

    /**
     * Przełącza stan zatrzymania zwierzęcia w komórce.
     * Aktualizuje wygląd (przezroczystość).
     */
    public synchronized void togglePause() {
        if (occupant != null) {
            paused = !paused;
            view.setOpacity(paused ? 0.3 : 1.0);
        }
    }
}