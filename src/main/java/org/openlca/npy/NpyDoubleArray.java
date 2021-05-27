package org.openlca.npy;

import java.util.Objects;

public final class NpyDoubleArray extends AbstractNpyArray {

  protected final double[] data;

  public NpyDoubleArray(int[] shape, double[] data, boolean fortranOrder) {
    super(shape, fortranOrder);
    this.data = Objects.requireNonNull(data);
  }

  public double[] data() {
    return data;
  }

  @Override
  public int size() {
    return data.length;
  }

  @Override
  public boolean isDoubleArray() {
    return true;
  }

  @Override
  public NpyDoubleArray asDoubleArray() {
    return this;
  }
}
