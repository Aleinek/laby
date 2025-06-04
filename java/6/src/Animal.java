/**
 * Abstrakcyjna klasa bazowa dla zwierząt w symulacji.
 * Zawiera wspólną logikę ruchu, opóźnienia i pozycjonowania.
 */
public abstract class Animal extends Thread {
    /** Plansza, na której porusza się zwierzę. */
    protected final Board board;
    /** Aktualna pozycja x zwierzęcia. */
    protected int x;
    /** Aktualna pozycja y zwierzęcia. */
    protected int y;
    /** Parametr opóźnienia ruchu. */
    protected final int delayK;
    /** Flaga określająca, czy zwierzę jest żywe. */
    protected volatile boolean alive = true;

    /**
     * Konstruktor zwierzęcia.
     * 
     * @param board   plansza symulacji
     * @param delayK  parametr opóźnienia
     */
    public Animal(Board board, int delayK) {
        this.board = board;
        this.delayK = delayK;
        placeRandomly();
    }

    /**
     * Umieszcza zwierzę na losowej pozycji na planszy.
     */
    protected void placeRandomly() {
        int x, y;
        do {
            x = Utils.random.nextInt(GUI.width);
            y = Utils.random.nextInt(GUI.height);
        } while (board.getCell(x, y).getOccupant() != null);
        board.addAnimal(this, x, y);
    }

    /**
     * Zwraca aktualną pozycję x zwierzęcia.
     * 
     * @return pozycja x
     */
    public synchronized int getX() { return x; }

    /**
     * Zwraca aktualną pozycję y zwierzęcia.
     * 
     * @return pozycja y
     */
    public synchronized int getY() { return y; }

    /**
     * Ustawia nową pozycję zwierzęcia.
     * 
     * @param x nowa pozycja x
     * @param y nowa pozycja y
     */
    public synchronized void setPosition(int x, int y) { this.x = x; this.y = y; }

    /**
     * Sprawdza, czy zwierzę jest zatrzymane (paused).
     * 
     * @return true jeśli zwierzę jest zatrzymane, false w przeciwnym razie
     */
    public synchronized boolean isPaused() { return board.getCell(x, y).isPaused(); }

    /**
     * Generuje losowe opóźnienie ruchu w zakresie [0.5k, 1.5k] ms.
     * Uwzględnia globalną pauzę symulacji.
     */
    protected void delay() {
        try {
            synchronized (board) {
                while (board.isPaused()) {
                    board.wait();
                }
            }
            int ms = delayK / 2 + Utils.random.nextInt(delayK);
            Thread.sleep(ms);
        } catch (InterruptedException ignored) { }
    }
}