package org.openlca.npy;

import java.util.Objects;

public abstract class NpyArray {

  protected final int[] shape;
  protected final boolean fortranOrder;

  protected NpyArray(int[] shape, boolean fortranOrder) {
    this.shape = Objects.requireNonNull(shape);
    this.fortranOrder = fortranOrder;
  }

  public final int[] shape() {
    return shape;
  }

  public final boolean hasFortranOrder() {
    return fortranOrder;
  }

  /**
   * Returns the size of this array. That is the number of elements in this
   * array.
   *
   * @return the number of elements of this array
   */
  public abstract int size();
}
