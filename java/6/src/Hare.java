/**
 * Klasa reprezentująca zająca w symulacji.
 * Implementuje logikę ucieczki przed wilkiem.
 */
public class Hare extends Animal {
    /**
     * Tworzy nowego zająca.
     * 
     * @param board  plansza symulacji
     * @param delayK parametr opóźnienia
     */
    public Hare(Board board, int delayK) {
        super(board, delayK);
    }

    /**
     * Główna pętla wątku zająca.
     * Wykonuje ruchy i sprawdza stan symulacji.
     */
    @Override
    public void run() {
        while (alive) {
            if (!isPaused()) {
                synchronized (this) {
                    if (!alive) break;
                }
                move();
            }
            delay();
        }
    }

    /**
     * Wykonuje pojedynczy ruch ucieczki.
     * Wybiera kierunek maksymalizujący dystans do wilka.
     */
    private void move() {
        Wolf wolf = board.getWolf();
        if (wolf == null) return;

        int bestDx = 0, bestDy = 0, maxDist = -1;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = x + dx, ny = y + dy;
                if (board.inBounds(nx, ny) && board.getCell(nx, ny).getOccupant() == null) {
                    int dist = Math.abs(nx - wolf.getX()) + Math.abs(ny - wolf.getY());
                    if (dist > maxDist) {
                        maxDist = dist;
                        bestDx = dx;
                        bestDy = dy;
                    }
                }
            }
        }
        board.move(this, x + bestDx, y + bestDy);
    }
}