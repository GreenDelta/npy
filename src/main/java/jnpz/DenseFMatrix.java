package jnpz;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
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
		return new int[]{rows, columns};
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
			byte[] headerBytes = (head.toString()).getBytes("ascii");

			// see https://docs.scipy.org/doc/numpy-1.14.2/neps/npy-format.html
			// 6 bytes: “x93NUMPY”
			// 1 byte: major version
			// 1 byte: minor version
			// 2 bytes: header length (as unsigned short)
			// h bytes: header
			// 1 byte: '\n'
			// + padding so that it is divisible by 16
			int unpadded = 10 + headerBytes.length + 1;
			int padding = 0;
			if ((unpadded % 16) != 0) {
				padding = 16 - (unpadded % 16);
			}

            MappedByteBuffer buf = channel.map(
                    FileChannel.MapMode.READ_WRITE, 0,
                    unpadded + padding + (rows * columns * 8));
            buf.order(ByteOrder.LITTLE_ENDIAN);

			// magic
            buf.put((byte) 0x93);
            buf.put("NUMPY".getBytes());
			// major version
            buf.put((byte) 0x01);
			// minor version
            buf.put((byte) 0x00);
			// header length
            buf.putShort((short) ((headerBytes.length + 1 + padding) & 0xffff));

            // header & padding
            buf.put(headerBytes);
			for (int i = 0; i < padding; i++) {
				buf.put((byte)' ');
			}
			buf.put((byte)'\n');

			// write the matrix data
            for (double val : data) {
                buf.putDouble(val);
            }
		} catch (IOException e) {
			throw new RuntimeException("failed to write to " + file, e);
		}
	}

	public static void main(String[] args) {
		File npy = new File("target/_j_dense_f_matrix_2.npy");
		if (!npy.getParentFile().exists()) {
			npy.getParentFile().mkdirs();
		}
		DenseFMatrix m = new DenseFMatrix(2, 3, new double[]{
				1., 2., 3., 4., 5., 6.
		});
		m.toNPY(npy);
	}
}
