public class Simulation {
    public static long startTime;

    public static void init(Board board, int hareCount, int delayK) {
        startTime = System.currentTimeMillis(); // Record the start time
        board.pauseSimulation(); // Start simulation in paused state
        for (int i = 0; i < hareCount; i++) {
            Hare hare = new Hare(board, delayK);
            hare.start();
        }
        Wolf wolf = new Wolf(board, delayK);
        board.setWolf(wolf);
        wolf.start();
    }
}