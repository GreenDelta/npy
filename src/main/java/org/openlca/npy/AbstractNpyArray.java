package org.openlca.npy;

import java.util.Objects;

abstract class AbstractNpyArray<T> implements NpyArray<T> {

  protected final int[] shape;
  protected final T data;
  protected final boolean fortranOrder;

  protected AbstractNpyArray(int[] shape, T data, boolean fortranOrder) {
    this.shape = Objects.requireNonNull(shape);
    this.data = Objects.requireNonNull(data);
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

  @Override
  public T data() {
    return data;
  }
}
