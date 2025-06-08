import java.util.List;
import java.util.ArrayList;

/**
 * Klasa reprezentująca wilka w symulacji.
 * Implementuje logikę polowania na zające.
 */
public class Wolf extends Animal {
    /** Liczba cykli odpoczynku po zjedzeniu zająca. */
    private int restCycles = 0;

    /**
     * Tworzy nowego wilka.
     * 
     * @param board  plansza symulacji
     * @param delayK parametr opóźnienia
     */
    public Wolf(Board board, int delayK) {
        super(board, delayK, true);
    }

    /**
     * Główna pętla wątku wilka.
     * Wykonuje ruchy, sprawdza warunki zakończenia symulacji.
     */
    @Override
    public void run() {
        while (true) {
            if (!isPaused()) {
                if (restCycles > 0) {
                    restCycles--;
                } else {
                    move();
                }
            }
            if (board.getHares().isEmpty()) {
                GUI.stopSimulation();
                break;
            }
            delay();
        }
    }

    /**
     * Wykonuje pojedynczy ruch w kierunku najbliższego zająca.
     * Jeśli złapie zająca, rozpoczyna cykl odpoczynku.
     */
    private void move() {
        // Najpierw sprawdzamy wszystkie sąsiednie pola w poszukiwaniu zająca do zjedzenia
        List<Hare> adjacentHares = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int ax = x + dx;
                int ay = y + dy;
                if (board.inBounds(ax, ay)) {
                    Cell cell = board.getCell(ax, ay);
                    if (cell.getOccupant() instanceof Hare h && !cell.isPaused()) {
                        adjacentHares.add(h);
                    }
                }
            }
        }

        // Jeśli znaleźliśmy sąsiadujące zające, zjadamy losowego
        if (!adjacentHares.isEmpty()) {
            Hare chosen = adjacentHares.get(Utils.random.nextInt(adjacentHares.size()));
            board.removeHare(chosen);
            // Próbujemy się ruszyć na miejsce zjedzonego zająca
            board.move(this, chosen.getX(), chosen.getY());
            restCycles = 5;
            return;
        }

        // Jeśli nie ma sąsiadujących zajęcy, próbujemy się ruszyć w kierunku najbliższego
        Hare target = board.getClosestHare(x, y);
        if (target == null) return;

        int dx = Integer.compare(target.getX(), x);
        int dy = Integer.compare(target.getY(), y);
        int nx = x + dx;
        int ny = y + dy;

        if (board.inBounds(nx, ny) && board.getCell(nx, ny).getOccupant() == null) {
            board.move(this, nx, ny);
        }
    }
}