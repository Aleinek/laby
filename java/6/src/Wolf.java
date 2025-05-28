public class Wolf extends Animal {
    private int restCycles = 0;

    public Wolf(Board board, int delayK) {
        super(board, delayK);
    }

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