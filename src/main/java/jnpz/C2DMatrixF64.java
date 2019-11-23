package jnpz;

public class C2DMatrixF64 {

    public final int rows;
    public final int columns;
    public final double[] data;

    public C2DMatrixF64(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.data = new double[rows * columns];
    }

    public int[] shape() {
        return new int[] {rows, columns};
    }
}
