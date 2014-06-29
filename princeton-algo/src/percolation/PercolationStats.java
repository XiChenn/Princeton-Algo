package percolation;
public class PercolationStats {
    private int n;        // n by n grid
    private int t;        // t independent experiments
    private double[] x;   // Results of t independent experiments  
    private double miu;   // mean
    private double sigma; // standard deviation
    
    // perform T independent computational experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        
        n = N;
        t = T;
        x = new double[t];
        for (int i = 0; i < t; i++) {
            int count = 0; // Count the opened sites
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) { // While not percolates
                int randomRow = (int) (Math.random() * n) + 1;
                int randomCol = (int) (Math.random() * n) + 1;
                if (!percolation.isOpen(randomRow, randomCol)) {
                    percolation.open(randomRow, randomCol); // Open a random site
                    count++; // Count++ only if the site is not opened
                }                
            }
            x[i] = (double) (count) / (n * n);
        }
        
        miu = mean();
        sigma = stddev();
    }

    // sample mean of percolation threshold
    public double mean() {
        double sum = 0.0;
        for (double value : x) {
            sum += value;
        }
        return sum / t;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        double sum = 0.0;
        for (double value : x) {
            sum += ((value - miu) * (value - miu));
        }
        return Math.sqrt(sum / (t - 1));
    }

    // returns lower bound of the 95% confidence interval
    public double confidenceLo() {
        return miu - 1.96 * sigma / Math.sqrt(t);
    }

    // returns upper bound of the 95% confidence interval
    public double confidenceHi() {
        return miu + 1.96 * sigma / Math.sqrt(t);
    }

    // test client, described below
    public static void main(String[] args) {
        final int N = Integer.valueOf(args[0]);
        final int T = Integer.valueOf(args[1]);

        PercolationStats stats = new PercolationStats(N, T);
        System.out.println("mean \t\t\t= " + stats.mean());
        System.out.println("stddev \t\t\t= " + stats.stddev());
        System.out.println("95% confidence interval = " + stats.confidenceLo()
                + ", " + stats.confidenceHi());
    }
}
