/**
 * Główna klasa zarządzająca symulacją.
 * Inicjalizuje planszę, zwierzęta i zapisuje czas rozpoczęcia.
 */
public class Simulation {
    /** Czas rozpoczęcia symulacji. */
    public static long startTime;

    /**
     * Inicjalizuje symulację.
     * 
     * @param board      plansza symulacji
     * @param hareCount  liczba zajęcy
     * @param delayK     parametr opóźnienia ruchów
     */
    public static void init(Board board, int hareCount, int delayK) {
        startTime = System.currentTimeMillis(); // Record the start time
        board.pauseSimulation(); // Start simulation in paused state
        for (int i = 0; i < hareCount+1; i++) {
            Hare hare = new Hare(board, delayK);
            hare.start();
        }
        Wolf wolf = new Wolf(board, delayK);
        board.setWolf(wolf);
        wolf.start();
    }
}