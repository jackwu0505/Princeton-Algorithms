import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private Percolation p;
    private int numTrials;   // number of trials
    private double mean;
    private double stddev;
    private double [] percoSites; // array to record the fraction of open sites that cause the system to percolate for each trial

    /* perform trials independent experiments on an n-by-n grid */
    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException("grid size:" + n + " needs to be greater than 0");  
        }
        if (trials <= 0) {
            throw new IllegalArgumentException("number of trials:" + n + " needs to be greater than 0");  
        }

        numTrials = trials;
        percoSites = new double[trials];
        for (int i = 0; i < trials; i++) {
            p = new Percolation(n);
            while(!p.percolates()) {
                int row = StdRandom.uniform(1,n+1);
                int col = StdRandom.uniform(1,n+1);

                if (p.isOpen(row, col))
                    continue;
                
                p.open(row, col);
                percoSites[i]++;
            }
            percoSites[i] = percoSites[i]/(n*n);           
        }
        mean = StdStats.mean(percoSites);
        stddev = StdStats.stddev(percoSites);
    }

    /* sample mean of percolation threshold */    
    public double mean() {
        return mean;
    }

    /* sample standard deviation of percolation threshold */                          
    public double stddev() {
        return stddev;
    }

    /* low  endpoint of 95% confidence interval */
    public double confidenceLo() {
        return mean - 1.96*stddev/Math.sqrt(numTrials);
    }

    /* high endpoint of 95% confidence interval */                  
    public double confidenceHi() {
        return mean + 1.96*stddev/Math.sqrt(numTrials);
    }                  
 
    /* test client (described below) */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats pStat = new PercolationStats(n, trials);
        StdOut.println("mean                    = " + pStat.mean());
        StdOut.println("stddev                  = " + pStat.stddev());
        StdOut.println("95% confidence interval = [" + pStat.confidenceLo() + ", " + pStat.confidenceHi() + "]");
    }        
 }