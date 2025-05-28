public class Simulation {
    public static void init(Board board, int hareCount, int delayK) {
        for (int i = 0; i < hareCount; i++) {
            Hare hare = new Hare(board, delayK);
            hare.start();
        }
        Wolf wolf = new Wolf(board, delayK);
        board.setWolf(wolf);
        wolf.start();
    }
}