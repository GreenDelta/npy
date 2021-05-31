package org.openlca.npy.arrays;

import java.util.Arrays;

public final class Array2D {

  private Array2D() {
  }

  /**
   * Checks if the given array is a valid 2-dimensional array. We do not check
   * this in the other utility methods of this class. So if you are not sure
   * if an array is a 2D array you should call this method to check this before
   * calling the other methods of this class.
   *
   * @param array the NPY array to check
   * @return {@code true} if the array has 2 dimensions and each dimensions is
   * {@code > 1}.
   */
  public static boolean isValid(NpyArray<?> array) {
    if (array == null || array.shape() == null)
      return false;
    var shape = array.shape();
    int rowCount = shape[0];
    int colCount = shape[1];
    return rowCount > 0
      && colCount > 0
      && rowCount * colCount == array.size();
  }

  public static int rowCountOf(NpyArray<?> array) {
    return array.shape()[0];
  }

  public static int columnCountOf(NpyArray<?> array) {
    return array.shape()[1];
  }

  public static int indexOf(NpyArray<?> array, int row, int col) {
    if (array.hasFortranOrder()) {
      int rows = array.shape()[0];
      return col * rows + row;
    } else {
      int cols = array.shape()[1];
      return row * cols + col;
    }
  }

  public static boolean get(NpyBooleanArray array, int row, int col) {
    int i = indexOf(array, row, col);
    return array.data[i];
  }

  public static boolean[] getRow(NpyBooleanArray array, int row) {
    int cols = array.shape[1];
    if (!array.hasFortranOrder()) {
      int offset = row * cols;
      return Arrays.copyOfRange(array.data, offset, offset + cols);
    }
    int rows = array.shape[0];
    boolean[] values = new boolean[cols];
    int offset = 0;
    for (int col = 0; col < cols; col++) {
      values[col] = array.data[offset + row];
      offset += rows;
    }
    return values;
  }

  public static boolean[] getColumn(NpyBooleanArray array, int col) {
    int rows = array.shape[0];
    if (array.hasFortranOrder()) {
      int offset = col * rows;
      return Arrays.copyOfRange(array.data, offset, offset + rows);
    }
    int cols = array.shape[1];
    boolean[] values = new boolean[rows];
    int offset = 0;
    for (int row = 0; row < rows; row++) {
      values[row] = array.data[offset + col];
      offset += cols;
    }
    return values;
  }

  public static byte get(NpyByteArray array, int row, int col) {
    int i = indexOf(array, row, col);
    return array.data[i];
  }

  public static byte[] getRow(NpyByteArray array, int row) {
    int cols = array.shape[1];
    if (!array.hasFortranOrder()) {
      int offset = row * cols;
      return Arrays.copyOfRange(array.data, offset, offset + cols);
    }
    int rows = array.shape[0];
    byte[] values = new byte[cols];
    int offset = 0;
    for (int col = 0; col < cols; col++) {
      values[col] = array.data[offset + row];
      offset += rows;
    }
    return values;
  }

  public static byte[] getColumn(NpyByteArray array, int col) {
    int rows = array.shape[0];
    if (array.hasFortranOrder()) {
      int offset = col * rows;
      return Arrays.copyOfRange(array.data, offset, offset + rows);
    }
    int cols = array.shape[1];
    byte[] values = new byte[rows];
    int offset = 0;
    for (int row = 0; row < rows; row++) {
      values[row] = array.data[offset + col];
      offset += cols;
    }
    return values;
  }

  public static char get(NpyCharArray array, int row, int col) {
    int i = indexOf(array, row, col);
    return array.data[i];
  }

  public static char[] getRow(NpyCharArray array, int row) {
    int cols = array.shape[1];
    if (!array.hasFortranOrder()) {
      int offset = row * cols;
      return Arrays.copyOfRange(array.data, offset, offset + cols);
    }
    int rows = array.shape[0];
    char[] values = new char[cols];
    int offset = 0;
    for (int col = 0; col < cols; col++) {
      values[col] = array.data[offset + row];
      offset += rows;
    }
    return values;
  }

  public static char[] getColumn(NpyCharArray array, int col) {
    int rows = array.shape[0];
    if (array.hasFortranOrder()) {
      int offset = col * rows;
      return Arrays.copyOfRange(array.data, offset, offset + rows);
    }
    int cols = array.shape[1];
    char[] values = new char[rows];
    int offset = 0;
    for (int row = 0; row < rows; row++) {
      values[row] = array.data[offset + col];
      offset += cols;
    }
    return values;
  }

  public static double get(NpyDoubleArray array, int row, int col) {
    int i = indexOf(array, row, col);
    return array.data[i];
  }

  public static double[] getRow(NpyDoubleArray array, int row) {
    int cols = array.shape[1];
    if (!array.hasFortranOrder()) {
      int offset = row * cols;
      return Arrays.copyOfRange(array.data, offset, offset + cols);
    }
    int rows = array.shape[0];
    double[] values = new double[cols];
    int offset = 0;
    for (int col = 0; col < cols; col++) {
      values[col] = array.data[offset + row];
      offset += rows;
    }
    return values;
  }

  public static double[] getColumn(NpyDoubleArray array, int col) {
    int rows = array.shape[0];
    if (array.hasFortranOrder()) {
      int offset = col * rows;
      return Arrays.copyOfRange(array.data, offset, offset + rows);
    }
    int cols = array.shape[1];
    double[] values = new double[rows];
    int offset = 0;
    for (int row = 0; row < rows; row++) {
      values[row] = array.data[offset + col];
      offset += cols;
    }
    return values;
  }

  public static float get(NpyFloatArray array, int row, int col) {
    int i = indexOf(array, row, col);
    return array.data[i];
  }

  public static float[] getRow(NpyFloatArray array, int row) {
    int cols = array.shape[1];
    if (!array.hasFortranOrder()) {
      int offset = row * cols;
      return Arrays.copyOfRange(array.data, offset, offset + cols);
    }
    int rows = array.shape[0];
    float[] values = new float[cols];
    int offset = 0;
    for (int col = 0; col < cols; col++) {
      values[col] = array.data[offset + row];
      offset += rows;
    }
    return values;
  }

  public static float[] getColumn(NpyFloatArray array, int col) {
    int rows = array.shape[0];
    if (array.hasFortranOrder()) {
      int offset = col * rows;
      return Arrays.copyOfRange(array.data, offset, offset + rows);
    }
    int cols = array.shape[1];
    float[] values = new float[rows];
    int offset = 0;
    for (int row = 0; row < rows; row++) {
      values[row] = array.data[offset + col];
      offset += cols;
    }
    return values;
  }

  public static int get(NpyIntArray array, int row, int col) {
    int i = indexOf(array, row, col);
    return array.data[i];
  }

  public static int[] getRow(NpyIntArray array, int row) {
    int cols = array.shape[1];
    if (!array.hasFortranOrder()) {
      int offset = row * cols;
      return Arrays.copyOfRange(array.data, offset, offset + cols);
    }
    int rows = array.shape[0];
    int[] values = new int[cols];
    int offset = 0;
    for (int col = 0; col < cols; col++) {
      values[col] = array.data[offset + row];
      offset += rows;
    }
    return values;
  }

  public static int[] getColumn(NpyIntArray array, int col) {
    int rows = array.shape[0];
    if (array.hasFortranOrder()) {
      int offset = col * rows;
      return Arrays.copyOfRange(array.data, offset, offset + rows);
    }
    int cols = array.shape[1];
    int[] values = new int[rows];
    int offset = 0;
    for (int row = 0; row < rows; row++) {
      values[row] = array.data[offset + col];
      offset += cols;
    }
    return values;
  }

  public static long get(NpyLongArray array, int row, int col) {
    int i = indexOf(array, row, col);
    return array.data[i];
  }

  public static long[] getRow(NpyLongArray array, int row) {
    int cols = array.shape[1];
    if (!array.hasFortranOrder()) {
      int offset = row * cols;
      return Arrays.copyOfRange(array.data, offset, offset + cols);
    }
    int rows = array.shape[0];
    long[] values = new long[cols];
    int offset = 0;
    for (int col = 0; col < cols; col++) {
      values[col] = array.data[offset + row];
      offset += rows;
    }
    return values;
  }

  public static long[] getColumn(NpyLongArray array, int col) {
    int rows = array.shape[0];
    if (array.hasFortranOrder()) {
      int offset = col * rows;
      return Arrays.copyOfRange(array.data, offset, offset + rows);
    }
    int cols = array.shape[1];
    long[] values = new long[rows];
    int offset = 0;
    for (int row = 0; row < rows; row++) {
      values[row] = array.data[offset + col];
      offset += cols;
    }
    return values;
  }

  public static short get(NpyShortArray array, int row, int col) {
    int i = indexOf(array, row, col);
    return array.data[i];
  }

  public static short[] getRow(NpyShortArray array, int row) {
    int cols = array.shape[1];
    if (!array.hasFortranOrder()) {
      int offset = row * cols;
      return Arrays.copyOfRange(array.data, offset, offset + cols);
    }
    int rows = array.shape[0];
    short[] values = new short[cols];
    int offset = 0;
    for (int col = 0; col < cols; col++) {
      values[col] = array.data[offset + row];
      offset += rows;
    }
    return values;
  }

  public static short[] getColumn(NpyShortArray array, int col) {
    int rows = array.shape[0];
    if (array.hasFortranOrder()) {
      int offset = col * rows;
      return Arrays.copyOfRange(array.data, offset, offset + rows);
    }
    int cols = array.shape[1];
    short[] values = new short[rows];
    int offset = 0;
    for (int row = 0; row < rows; row++) {
      values[row] = array.data[offset + col];
      offset += cols;
    }
    return values;
  }

}
