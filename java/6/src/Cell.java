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

    /** Statyczny obrazek zająca (ładowany tylko raz). */
    private static final Image hareImage = new Image("resources/Hare.png");
    /** Statyczny obrazek wilka (ładowany tylko raz). */
    private static final Image wolfImage = new Image("resources/Wolf.png");

    /**
     * Tworzy komórkę o podanych współrzędnych i rozmiarze.
     * 
     * @param x pozycja x (nieużywana wizualnie, ale informacyjnie)
     * @param y pozycja y (nieużywana wizualnie, ale informacyjnie)
     * @param cellSize rozmiar komórki
     */
    public Cell(int x, int y, int cellSize) {
        this.view = new Rectangle(cellSize, cellSize, Color.LIGHTGRAY);
        this.view.setStroke(Color.BLACK);
    }

    // Stary konstruktor dla kompatybilności (opcjonalnie można usunąć)
    public Cell(int x, int y) {
        this(x, y, GUI.cellSize);
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
            view.setFill(new ImagePattern(hareImage));
        } else if (a instanceof Wolf) {
            view.setFill(new ImagePattern(wolfImage));
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