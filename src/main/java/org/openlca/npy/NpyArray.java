package org.openlca.npy;

public interface NpyArray<T> {

  T data();

  int[] shape();

  /**
   * Returns if this array is stored in Fortran order (column-major order). If
   * false is returned it is stored in C order (row-major order).
   *
   * @return {@code true} if the array is stored in Fortran order; {@code false}
   * otherwise
   */
  boolean hasFortranOrder();

  /**
   * Returns the size of this array. That is the number of elements in this
   * array.
   *
   * @return the number of elements of this array
   */
  int size();

  default boolean isDoubleArray() {
    return false;
  }

  NpyDoubleArray asDoubleArray();

}
