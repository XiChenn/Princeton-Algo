package percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int n; // N-by-N grid
    private boolean[] isOpened; // Whether a site is opened
    private WeightedQuickUnionUF unionFind;  // Has 2 virtual nodes
    private WeightedQuickUnionUF unionFind2; // Has 1 virtual nodes-for backwash

    // Create N-by-N grid, with all sites blocked - O(n^2)
    public Percolation(int N) {
        if (N < 1) {
            throw new IllegalArgumentException();
        }
        n = N;
        isOpened = new boolean[n * n];
        unionFind = new WeightedQuickUnionUF(n * n + 2);       
        unionFind2 = new WeightedQuickUnionUF(n * n + 1);
    }

    // Open site (row i, column j) if it is not already
    public void open(int i, int j) {
        int index = convert(i, j);
        isOpened[index] = true;
   
        if (i == n) { // Last row, union it to the virtual lower site
            unionFind.union(index, n * n + 1);
        }
        if (i == 1) { // First row
            unionFind.union(index, n * n); 
            unionFind2.union(index, n * n);
        }
        
        // Update left, right, upper and lower sites;
        updateUnionFind(j - 1 >= 1 && isOpen(i, j - 1), index, i, j - 1);     
        updateUnionFind(j + 1 <= n && isOpen(i, j + 1), index, i, j + 1);
        updateUnionFind(i - 1 >= 1 && isOpen(i - 1, j), index, i - 1, j);
        updateUnionFind(i + 1 <= n && isOpen(i + 1, j), index, i + 1, j);
    }
    
    // Update unionFind data structure according to the condition
    private void updateUnionFind(boolean condition, int index, int i, int j) {
        if (condition) {
            int index2 = convert(i, j);
            unionFind.union(index, index2);
            unionFind2.union(index, index2);
        }
    }

    // Is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        return isOpened[convert(i, j)];
    }

    // Is site (row i, column j) full (water can flow from top row to this site)
    public boolean isFull(int i, int j) {
        return isOpen(i, j) && unionFind2.connected(convert(i, j), n * n);
    }

    // Does the system percolate?
    public boolean percolates() {
        return unionFind.connected(n * n, n * n + 1);
    }

    // Convert 2-D coordinate to 1-D - (1, 1) is the upper-left site
    private int convert(int i, int j) {
        if (i >= 1 && i <= n && j >= 1 && j <= n) {
            return (i - 1) * n + (j - 1);
        }
        throw new IndexOutOfBoundsException();
    }
}
