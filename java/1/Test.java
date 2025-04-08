public class Test {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Nie podano argumentow");
            return;
        }else{
            int i = 0;
            try {
                int n = Integer.parseInt(args[i]);
                if (n <= 0) {
                    System.out.println(args[i] + " - Podano liczbe niedodatnia");
                    return;
                }
                WierszTrojkataPascala Pascal = new WierszTrojkataPascala(n); 
                    
                for (i = 1; i < args.length; i++){
                    try {
                    int k = Integer.parseInt(args[i]);

                    if (k < 0 || k > n) {
                        System.out.println(args[i] + " - Podano liczbe spoza zakresu");
                        continue;
                    }
                    System.out.println(args[i] + " - " + Pascal.triangle[k]);
                    } catch (NumberFormatException ex) {
                        System.out.println(args[i] + " - Podany argument nie jest liczba calkowita");
                        continue;
                    }
                    
                }                
            }
            catch (NumberFormatException ex) {
                System.out.println(args[i] + " - Podana liczba nie jest liczba calkowita");
            }
            
        }

    }
}
