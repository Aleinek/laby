import java.util.Random;

/**
 * Klasa narzędziowa z współdzielonymi zasobami.
 */
public class Utils {
    /**
     * Współdzielony generator liczb pseudolosowych.
     * Używany przez wszystkie komponenty symulacji.
     */
    public static final Random random = new Random();
}