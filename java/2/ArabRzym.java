
public class ArabRzym {
    private final static int MIN = 0;
    private final static int MAX = 3999;

    private static String[] liczby = { "I","IV","V","IX","X","XL","L","XC","C","CD","D","CM","M" };
    private static int[] wartosci = { 1,4,5,9,10,40,50,90,100,400,500,900,1000 };

    public static int rzym2arab (String rzym) throws ArabRzymException {
        int wynik = 0;
        int i = 0;
        int j = 12;

        int ile = 0;
        while (i < rzym.length()) {
            if (j>=0){
                if (rzym.substring(i).startsWith(liczby[j])) {
                    wynik += wartosci[j];
                    i += liczby[j].length();

                    ile++;
                    if (ile > 3){
                        throw new ArabRzymException("Nieprawidłowy format liczby rzymskiej");
                    }

                    if (liczby[j].length() == 2){
                        j--;
                        ile = 0;
                    }

                    if (j == 2 || j == 6 || j == 11){
                        j -= 2;
                        ile = 0;
                    }
                }
                else{
                    j--;
                    ile = 0;
                }
            }
            else{
                throw new ArabRzymException("Nieprawidłowy format liczby rzymskiej");
            }
        }

        if (wynik < MIN || wynik > MAX) {
            throw new ArabRzymException("Liczba spoza zakresu");
        }
        return wynik;
    }
    public static String arab2rzym (int arab) throws ArabRzymException {
        if (arab < MIN || arab > MAX) {
            throw new ArabRzymException("Liczba spoza zakresu");
        }
        String wynik = "";
        int i = 12;
        while (arab > 0) {
            if (wartosci[i] <= arab) {
                wynik += liczby[i];
                arab -= wartosci[i];
            }
            else {
                i--;
            }
        }
        return wynik;
    }
    }