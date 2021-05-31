package org.openlca.npy.arrays;

public final class NpyDoubleArray extends AbstractNpyArray<double[]> {

  public NpyDoubleArray(int[] shape, double[] data, boolean fortranOrder) {
    super(shape, data, fortranOrder);
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

  @Override
  public NpyFloatArray asFloatArray() {
    var floats = new float[data.length];
    for (int i = 0; i < data.length; i++) {
      floats[i] = (float) data[i];
    }
    return new NpyFloatArray(copyShape(), floats, fortranOrder);
  }

  @Override
  public NpyIntArray asIntArray() {
    var ints = new int[data.length];
    for (int i = 0; i < data.length; i++) {
      ints[i] = (int) data[i];
    }
    return new NpyIntArray(copyShape(), ints, fortranOrder);
  }
}
