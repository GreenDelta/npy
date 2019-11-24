package jnpz;

public interface Array2D {

    int[] shape();    

    double[] row(int i);

    double[] column(int j);

    double entry(int i, int j);

}