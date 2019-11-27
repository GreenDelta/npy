package jnpz;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class DenseFMatrix implements Array2D {

    public final int rows;
    public final int columns;
    public final double[] data;

    public DenseFMatrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.data = new double[rows * columns];
    }

    public DenseFMatrix(int rows, int columns, double[] data) {
        this.rows = rows;
        this.columns = columns;
        this.data = data;
    }

    @Override
    public int[] shape() {
        return new int[] {rows, columns};
    }

    @Override
    public double get(int i, int j) {
        int idx = rows * j + i;
        return data[idx];
    }

    @Override
    public double[] getColumn(int j) {
        int offset = rows * j;
        double[] col = new double[rows];
        System.arraycopy(data, offset, col, 0, rows);
        return col;
    }

    @Override
    public double[] getRow(int i) {
        double[] row = new double[columns];
        for (int j = 0; j < columns; j++) {
            int idx = rows * j + i;
            row[j] = data[idx];
        }
        return row;
    }

    public void toNPY(File file) {
        try (RandomAccessFile f = new RandomAccessFile(file, "rw");
             FileChannel channel = f.getChannel()) {

            Header head = new Header();
            head.dtype = "<f8";
            head.shape = shape();
            head.fortranOrder = true;
            byte[] headerBytes = (head.toString() + "\n").getBytes("utf-8");



        } catch (IOException e) {
            throw new RuntimeException("failed to write to " + file, e);
        }
    }
}
