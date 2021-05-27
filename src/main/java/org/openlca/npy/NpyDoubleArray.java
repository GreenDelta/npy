package org.openlca.npy;

import java.util.Objects;

public class NpyDoubleArray extends NpyArray {

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
}
