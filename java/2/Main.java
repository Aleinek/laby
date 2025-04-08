
class Main{
    public static void main(String[] args){
        for (int i = 0; i < args.length; i++){
            try{
                try{
                    System.out.println(ArabRzym.arab2rzym(Integer.parseInt(args[i])));
                }
                catch(NumberFormatException error){
                    System.out.println(ArabRzym.rzym2arab(args[i]));
                }
            }
            catch(ArabRzymException error){
                System.out.println(error.getMessage());
            }  
        }
    }
}