import java.util.*;

public class Board {
    private final Cell[][] cells;
    private final int width, height;
    private final List<Hare> hares = Collections.synchronizedList(new ArrayList<>());

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                cells[x][y] = new Cell(x, y);
    }

    public synchronized boolean move(Animal a, int newX, int newY) {
        if (!inBounds(newX, newY) || cells[newX][newY].getOccupant() != null)
            return false;
        cells[a.getX()][a.getY()].clear();
        a.setPosition(newX, newY);
        cells[newX][newY].setOccupant(a);
        return true;
    }

    public synchronized void addAnimal(Animal a, int x, int y) {
        cells[x][y].setOccupant(a);
        a.setPosition(x, y);
        if (a instanceof Hare hare) hares.add(hare);
    }

    public synchronized void removeHare(Hare h) {
        hares.remove(h);
        cells[h.getX()][h.getY()].clear();
    }

    public synchronized List<Hare> getHares() {
        return new ArrayList<>(hares);
    }

    public synchronized Hare getClosestHare(int x, int y) {
        return hares.stream().min(Comparator.comparingInt(h -> Math.abs(h.getX() - x) + Math.abs(h.getY() - y))).orElse(null);
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }
}