public class WierszTrojkataPascala{
    public int[] triangle;
    WierszTrojkataPascala(int n){
        triangle = new int[n+1];
        triangle[0] = 1;
        
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j > 0; j--) {
                triangle[j] += triangle[j - 1];
            }
        }
    }
}