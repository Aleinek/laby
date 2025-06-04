import java.util.*;

/**
 * Klasa reprezentująca planszę symulacji.
 * Zarządza komórkami, zwierzętami i synchronizacją.
 */
public class Board {
    /** Dwuwymiarowa tablica komórek. */
    private final Cell[][] cells;
    /** Szerokość planszy. */
    private final int width;
    /** Wysokość planszy. */
    private final int height;
    /** Lista zajęcy na planszy (synchronizowana). */
    private final List<Hare> hares = Collections.synchronizedList(new ArrayList<>());
    /** Wilk na planszy. */
    private Wolf wolf;
    /** Flaga pauzy symulacji. */
    private volatile boolean paused = false;

    // Konstruktor z cellSize (do skalowania)
    public Board(int width, int height, int cellSize) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                cells[x][y] = new Cell(x, y, cellSize);
    }

    // Stary konstruktor dla kompatybilności
    public Board(int width, int height) {
        this(width, height, GUI.cellSize);
    }

    /**
     * Próbuje przesunąć zwierzę na nową pozycję.
     * 
     * @param a     zwierzę do przesunięcia
     * @param newX  nowa pozycja x
     * @param newY  nowa pozycja y
     * @return true, jeśli ruch się powiódł; false, jeśli ruch jest niemożliwy
     */
    public synchronized boolean move(Animal a, int newX, int newY) {
        if (!inBounds(newX, newY) || cells[newX][newY].getOccupant() != null)
            return false;
        cells[a.getX()][a.getY()].clear();
        a.setPosition(newX, newY);
        cells[newX][newY].setOccupant(a);
        return true;
    }

    /**
     * Dodaje zwierzę na planszę na podane pozycje.
     * 
     * @param a zwierzę do dodania
     * @param x pozycja x
     * @param y pozycja y
     */
    public synchronized void addAnimal(Animal a, int x, int y) {
        cells[x][y].setOccupant(a);
        a.setPosition(x, y);
        if (a instanceof Hare hare) hares.add(hare);
    }

    /**
     * Usuwa zająca z planszy.
     * 
     * @param h zając do usunięcia
     */
    public synchronized void removeHare(Hare h) {
        h.alive = false;
        hares.remove(h);
        cells[h.getX()][h.getY()].clear();
    }

    /**
     * Zwraca kopię listy zajęcy (bezpieczna wątkowo).
     * 
     * @return lista zajęcy
     */
    public synchronized List<Hare> getHares() {
        return new ArrayList<>(hares);
    }

    /**
     * Znajduje najbliższego zająca dla danej pozycji (w metryce Manhattan).
     * 
     * @param x pozycja x
     * @param y pozycja y
     * @return najbliższy zając lub null, jeśli nie ma zajęcy
     */
    public synchronized Hare getClosestHare(int x, int y) {
        return hares.stream()
            .filter(h -> !getCell(h.getX(), h.getY()).isPaused())
            .min(Comparator.comparingInt(h -> Math.abs(h.getX() - x) + Math.abs(h.getY() - y)))
            .orElse(null);
    }

    /**
     * Sprawdza, czy podana pozycja znajduje się w granicach planszy.
     * 
     * @param x pozycja x
     * @param y pozycja y
     * @return true, jeśli pozycja jest w granicach; false w przeciwnym razie
     */
    public boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    /**
     * Zwraca komórkę o podanych współrzędnych.
     * 
     * @param x pozycja x
     * @param y pozycja y
     * @return komórka na pozycji (x, y)
     */
    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    /**
     * Zwraca wilka z planszy.
     * 
     * @return wilk
     */
    public synchronized Wolf getWolf() {
        return wolf;
    }

    /**
     * Ustawia wilka na planszy.
     * 
     * @param wolf wilk
     */
    public synchronized void setWolf(Wolf wolf) {
        this.wolf = wolf;
        int x = wolf.getX();
        int y = wolf.getY();
        cells[x][y].setOccupant(wolf);
    }

    /**
     * Wstrzymuje symulację.
     */
    public synchronized void pauseSimulation() {
        paused = true;
    }

    /**
     * Wznawia symulację.
     */
    public synchronized void resumeSimulation() {
        paused = false;
        notifyAll();
    }

    /**
     * Sprawdza, czy symulacja jest wstrzymana.
     * 
     * @return true, jeśli symulacja jest wstrzymana; false w przeciwnym razie
     */
    public synchronized boolean isPaused() {
        return paused;
    }
}