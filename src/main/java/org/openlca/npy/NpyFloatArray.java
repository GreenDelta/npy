package org.openlca.npy;

import java.util.Arrays;

public class NpyFloatArray extends AbstractNpyArray<float[]> {

  public NpyFloatArray(int[] shape, float[] data, boolean fortranOrder) {
    super(shape, data, fortranOrder);
  }

  @Override
  public int size() {
    return data.length;
  }

  @Override
  public NpyDoubleArray asDoubleArray() {
    var doubles = new double[data.length];
    for (int i = 0; i < data.length; i++) {
      doubles[i] = data[i];
    }
    return new NpyDoubleArray(
      Arrays.copyOf(shape, shape.length), doubles, fortranOrder);
  }
}
