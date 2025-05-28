public class Hare extends Animal {
    public Hare(Board board, int delayK) {
        super(board, delayK);
    }

    @Override
    public void run() {
        while (alive) {
            if (!isPaused()) move();
            delay();
        }
    }

    private void move() {
        Hare me = this;
        Wolf wolf = (Wolf) board.getCell(getX(), getY()).getOccupant();
        Hare nearest = board.getClosestHare(x, y);
        if (nearest == null) return;

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