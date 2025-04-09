import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Object> figury = new ArrayList<>();

        for (int i = 0; i < args.length; ) {
            String typ = args[i];
            try {
                switch (typ) {
                    case "o":
                        double promien = Double.parseDouble(args[i + 1]);

                        JednoParametroweFigury kolo = JednoParametroweFigury.KOLO;
                        kolo.setParametr(promien);
                        figury.add(kolo);

                        i += 2;
                        break;
                    case "c":
                        int paramsCount = 0;
                        while(i+paramsCount+1 < args.length && 
                                !Arrays.asList("o", "c", "p", "s").contains(args[i+paramsCount+1])) {
                            paramsCount ++;
                        }

                        if (paramsCount == 5) {
                            double bok1 = Double.parseDouble(args[i + 1]);
                            double bok2 = Double.parseDouble(args[i + 2]);
                            double bok3 = Double.parseDouble(args[i + 3]);
                            double bok4 = Double.parseDouble(args[i + 4]);
                            double kat = Double.parseDouble(args[i + 5]);

                            if (kat == 90){
                                if (bok1 <= 0 || bok2 <= 0 || bok3 <= 0 || bok4 <= 0 || kat <= 0) {
                                    throw new ArrayIndexOutOfBoundsException("Boki i kąt muszą być większe od zera.");
                                }
                                if (bok1 == bok3 && bok2 == bok4 || bok1 == bok4 && bok2 == bok3) {
                                    DwuParametroweFigury prostokat = DwuParametroweFigury.PROSTOKAT;
                                    prostokat.setParametry(bok1, bok2);
                                    figury.add(prostokat);
                                }else{
                                    throw new ArrayIndexOutOfBoundsException("Boki muszą być równe.");
                                }
                            }

                            i += 6;
                        } else if (paramsCount == 2) {
                            double bok1 = Double.parseDouble(args[i + 1]);
                            double kat = Double.parseDouble(args[i + 2]);

                            if (bok1 <= 0 || kat <= 0) {
                                throw new ArrayIndexOutOfBoundsException("Boki i kąt muszą być większe od zera.");
                            }
                            if (kat == 90){
                                JednoParametroweFigury kwadrat = JednoParametroweFigury.KWADRAT;
                                kwadrat.setParametr(bok1);
                                figury.add(kwadrat);
                            } else {
                                DwuParametroweFigury romb = DwuParametroweFigury.ROMB;
                                romb.setParametry(bok1, kat);
                                figury.add(romb);
                            }
                            i += 3;
                        } else {
                            throw new IllegalArgumentException("a");
                        }
                        break;
                    case "p":
                        double bok5 = Double.parseDouble(args[i + 1]);
                        JednoParametroweFigury pieciokat = JednoParametroweFigury.PIECIOKAT;
                        pieciokat.setParametr(bok5);
                        figury.add(pieciokat);
                        i += 2;
                        break;
                    case "s":
                        double bok6 = Double.parseDouble(args[i + 1]);
                        JednoParametroweFigury szesciokat = JednoParametroweFigury.SZESCIOKAT;
                        szesciokat.setParametr(bok6);
                        figury.add(szesciokat);
                        i += 2;
                        break;
                    default:
                        System.out.println("Nieznany typ figury.");
                        return;

                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Błąd: " + e.getMessage());
                return;
            }
        }

        for (Object figura : figury) {
            if (figura instanceof JednoParametroweFigury) {
                JednoParametroweFigury f = (JednoParametroweFigury) figura;
                System.out.println("Figura: " + f.podajNazwe());
                System.out.println("Pole: " + f.obliczPole());
                System.out.println("Obwód: " + f.obliczObwod());
            } else if (figura instanceof DwuParametroweFigury) {
                DwuParametroweFigury f = (DwuParametroweFigury) figura;
                System.out.println("Figura: " + f.podajNazwe());
                System.out.println("Pole: " + f.obliczPole());
                System.out.println("Obwód: " + f.obliczObwod());
            }
        }
    }
}