package jnpz;

public interface Array2D extends ArrayND {

    /**
     * Get the values of the row with the given index i.
     */
    double[] getRow(int i);

    /**
     * Get the values of the column with the given index j.
     */
    double[] getColumn(int j);

    /**
     * Get the value of the entry at row i and column j.
     */
    double get(int i, int j);

}