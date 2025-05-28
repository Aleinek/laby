public abstract class Animal extends Thread {
    protected final Board board;
    protected int x, y;
    protected final int delayK;
    protected boolean alive = true;

    public Animal(Board board, int delayK) {
        this.board = board;
        this.delayK = delayK;
        placeRandomly();
    }

    protected void placeRandomly() {
        int x, y;
        do {
            x = Utils.random.nextInt(GUI.width);
            y = Utils.random.nextInt(GUI.height);
        } while (board.getCell(x, y).getOccupant() != null);
        board.addAnimal(this, x, y);
    }

    public synchronized int getX() { return x; }
    public synchronized int getY() { return y; }
    public synchronized void setPosition(int x, int y) { this.x = x; this.y = y; }
    public synchronized boolean isPaused() { return board.getCell(x, y).isPaused(); }

    protected void delay() {
        try {
            int ms = delayK / 2 + Utils.random.nextInt(delayK);
            Thread.sleep(ms);
        } catch (InterruptedException ignored) { }
    }
}