package org.openlca.npy;

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
  public static boolean isValid(NpyArray array) {
    if (array == null || array.shape == null || array.shape.length != 2)
      return false;
    int rowCount = array.shape[0];
    int colCount = array.shape[1];
    return rowCount > 0
           && colCount > 0
           && rowCount * colCount == array.size();
  }

  public static int rowCountOf(NpyArray array) {
    return array.shape[0];
  }

  public static int columnCountOf(NpyArray array) {
    return array.shape[1];
  }

  public static int indexOf(NpyArray array, int row, int col) {
    if (array.hasFortranOrder()) {
      int rows = array.shape[0];
      return col * rows + row;
    } else {
      int cols = array.shape[1];
      return row * cols + col;
    }
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

}
