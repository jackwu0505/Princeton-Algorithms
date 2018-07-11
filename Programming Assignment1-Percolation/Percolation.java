import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF uf1; // used for isFull() 
    private WeightedQuickUnionUF uf2; // used for percolates()
    private int topSiteId;    // top site to detect system percolation
    private int bottomSiteId; // bottom site to detect system percolation
    private int [][] siteId;  // ID for each site in n-by-n grid
    private boolean [][] siteState; // records whether the site is opened(true) or blocked(false) 
    private int openSites;  // number of open sites
    private int gridSize;   // n-by-n grid size
    

    /* create n-by-n grid, with all sites blocked */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("grid size:" + n + " needs to be greater than 0");  
        }

        siteId = new int[n][n];
        siteState = new boolean[n][n];
        uf1 = new WeightedQuickUnionUF(n*n+1); // n-by-n grid + top site -> used for isFull()
        uf2 = new WeightedQuickUnionUF(n*n+2); // n-by-n grid + top and bottom site --> used for percolates()
        gridSize = n;
        
        // initialze site id
        int id = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                siteState[i][j] = false;
                siteId[i][j] = id;
                id++;
            }
        }
        topSiteId = 0;
        bottomSiteId = n*n + 1;
    }
    
    /* open site (row, col) if it is not open already */
    public void open(int row, int col) {
        row = row - 1; col = col - 1;
        if (row < 0 || row >= gridSize) {
            throw new IllegalArgumentException("row index " + row + " is not between 0 and " + (gridSize-1));  
        }
        if (col < 0 || col >= gridSize) {
            throw new IllegalArgumentException("column index " + col + " is not between 0 and " + (gridSize-1));  
        }

        if (siteState[row][col] == false) {
            siteState[row][col] = true;
            openSites++;
        
            // site above
            try {
                if (siteState[row-1][col] == true) {
                    uf1.union(siteId[row][col], siteId[row-1][col]);
                    uf2.union(siteId[row][col], siteId[row-1][col]);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                // do nothing
            }
            // site below
            try {
                if (siteState[row+1][col] == true) {
                    uf1.union(siteId[row][col], siteId[row+1][col]);
                    uf2.union(siteId[row][col], siteId[row+1][col]);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                // do nothing
            }
            // left site
            try {
                if (siteState[row][col-1] == true) {
                    uf1.union(siteId[row][col], siteId[row][col-1]);
                    uf2.union(siteId[row][col], siteId[row][col-1]);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                // do nothing
            }
            // right site
            try {
                if (siteState[row][col+1] == true) {
                    uf1.union(siteId[row][col], siteId[row][col+1]);
                    uf2.union(siteId[row][col], siteId[row][col+1]);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                // do nothing
            }

            // connect top site to the top row of the grid if site opened
            if (row == 0) {
                uf1.union(topSiteId, siteId[row][col]);
                uf2.union(topSiteId, siteId[row][col]);
            }

            // connect bottom site to the bottom row of the grid if site opened
            if (row == gridSize-1)
                uf2.union(bottomSiteId, siteId[row][col]);
        }
    }  

    /* is site (row, col) open? */
    public boolean isOpen(int row, int col) {
        row = row - 1; col = col - 1;
        if (row < 0 || row >= gridSize) {
            throw new IllegalArgumentException("row index " + row + " is not between 0 and " + (gridSize-1));  
        }
        if (col < 0 || col >= gridSize) {
            throw new IllegalArgumentException("column index " + col + " is not between 0 and " + (gridSize-1));  
        }

        return siteState[row][col];
    }

    /* is site (row, col) full? */
    public boolean isFull(int row, int col) {
        row = row - 1; col = col - 1;
        if (row < 0 || row >= gridSize) {
            throw new IllegalArgumentException("row index " + row + " is not between 0 and " + (gridSize-1));  
        }
        if (col < 0 || col >= gridSize) {
            throw new IllegalArgumentException("column index " + col + " is not between 0 and " + (gridSize-1));  
        }

        // blocked sites cannot be full
        if (siteState[row][col] == false)
            return false;
        else
            return uf1.connected(topSiteId,siteId[row][col]);
    }

    /* number of open sites */
    public int numberOfOpenSites() {
        return openSites;
    }

    /* does the system percolate? */
    public boolean percolates() {
        return uf2.connected(topSiteId,bottomSiteId);
    }
    
    /* test client (optional) */
    public static void main(String[] args) {
        int n = StdIn.readInt();
        Percolation p = new Percolation(n);
        while (!StdIn.isEmpty()) {
            int row = StdIn.readInt();
            int col = StdIn.readInt();
            p.open(row, col);
        }
        StdOut.println(p.numberOfOpenSites() + " open sites");
        StdOut.println("System percolation: " + p.percolates());
    }
}
