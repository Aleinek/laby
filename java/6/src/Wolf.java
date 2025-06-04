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
        super(board, delayK);
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
        Hare target = board.getClosestHare(x, y);
        if (target == null) return;

        int dx = Integer.compare(target.getX(), x);
        int dy = Integer.compare(target.getY(), y);
        int nx = x + dx;
        int ny = y + dy;

        if (board.inBounds(nx, ny)) {
            Cell cell = board.getCell(nx, ny);
            if (cell.getOccupant() instanceof Hare h && !cell.isPaused()) {
                board.removeHare(h);
                restCycles = 5;
            }
            board.move(this, nx, ny);
        }
    }
}