import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Figura> figury = new ArrayList<>();

        for (int i = 0; i < args.length; ) {
            String typ = args[i];
            try {
                switch (typ) {
                    case "o":
                        double promien = Double.parseDouble(args[i + 1]);
                        figury.add(new Kolo(promien));
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
                                    throw new IllegalArgumentException("Boki i kąt muszą być większe od zera.");
                                }
                                if (bok1 == bok3 && bok2 == bok4 || bok1 == bok4 && bok2 == bok3) {
                                    figury.add(new Prostokat(bok1, bok2));
                                }else if (bok1 == bok2 && bok3 == bok4){
                                    figury.add(new Prostokat(bok1, bok3));
                                }
                                else{
                                    throw new IllegalArgumentException("Boki muszą być równe.");
                                }
                            }

                            i += 6;
                        } else if (paramsCount == 2) {
                            double bok1 = Double.parseDouble(args[i + 1]);
                            double kat = Double.parseDouble(args[i + 2]);

                            if (bok1 <= 0 || kat <= 0) {
                                throw new IllegalArgumentException("Boki i kąt muszą być większe od zera.");
                            }
                            if (kat == 90){
                                figury.add(new Kwadrat(bok1));
                            } else {
                                figury.add(new Romb(bok1, kat));
                            }
                            i += 3;
                        } else {
                            throw new IllegalArgumentException("a");
                        }
                        break;
                    case "p":
                        double bok5 = Double.parseDouble(args[i + 1]);
                        figury.add(new Pieciokat(bok5));
                        i += 2;
                        break;
                    case "s":
                        double bok6 = Double.parseDouble(args[i + 1]);
                        figury.add(new Szesciokat(bok6));
                        i += 2;
                        break;
                    default:
                        System.out.println("Nieznany typ figury.");
                        return;
                }
            } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                System.out.println("Błąd: " + e.getMessage());
                return;
            }
        }

        for (Figura figura : figury) {
            System.out.println(figura.nazwa() + " - Pole: " + figura.pole() + ", Obwód: " + figura.obwod());
        }
    }
}