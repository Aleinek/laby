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
    public Animal(Board board, int delayK, boolean isWolf) {
        this.board = board;
        this.delayK = delayK;
        if (isWolf) {
            this.x = 0; 
            this.y = 0;
        } else {
            placeRandomly();
        }
        
    }

    /**
     * Umieszcza zwierzę na losowej pozycji na planszy.
     */
    protected void placeRandomly() {
        synchronized (board) {
            int maxAttempts = GUI.width * GUI.height;
            int attempts = 0;
            while (attempts < maxAttempts) {
                int x = Utils.random.nextInt(GUI.width);
                int y = Utils.random.nextInt(GUI.height);
                Cell cell = board.getCell(x, y);
                synchronized (cell) {
                    if (cell.getOccupant() == null) {
                        board.addAnimal(this, x, y);
                        return;
                    }
                }
                attempts++;
            }
            for (int x = 0; x < GUI.width; x++) {
                for (int y = 0; y < GUI.height; y++) {
                    Cell cell = board.getCell(x, y);
                    synchronized (cell) {
                        if (cell.getOccupant() == null) {
                            board.addAnimal(this, x, y);
                            return;
                        }
                    }
                }
            }
            throw new RuntimeException("Brak wolnego miejsca na planszy!");
        }
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