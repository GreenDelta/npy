package org.openlca.npy.arrays;

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

  default boolean isBooleanArray() {
    return false;
  }

  NpyBooleanArray asBooleanArray();

  default boolean isDoubleArray() {
    return false;
  }

  /**
   * Convert this array into a double array. If this array is already a double
   * array it is directly returned without making a copy of it.
   *
   * @return this array if it is a double array, otherwise a converted array
   */
  NpyDoubleArray asDoubleArray();

  default boolean isFloatArray() {
    return false;
  }

  NpyFloatArray asFloatArray();

  default boolean isIntArray() {
    return false;
  }

  NpyIntArray asIntArray();

  default boolean isLongArray() {
    return false;
  }

  NpyLongArray asLongArray();

}
