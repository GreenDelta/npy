package org.openlca.npy;

import java.util.Objects;

abstract class AbstractNpyArray implements NpyArray {

  protected final int[] shape;
  protected final boolean fortranOrder;

  protected AbstractNpyArray(int[] shape, boolean fortranOrder) {
    this.shape = Objects.requireNonNull(shape);
    this.fortranOrder = fortranOrder;
  }

  @Override
  public final int[] shape() {
    return shape;
  }

  @Override
  public final boolean hasFortranOrder() {
    return fortranOrder;
  }

}
