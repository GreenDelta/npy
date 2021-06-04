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
  public NpyBooleanArray asBooleanArray() {
    var booleans = new boolean[data.length];
    for (int i = 0; i < data.length; i++) {
      booleans[i] = i != 0;
    }
    return new NpyBooleanArray(copyShape(), booleans, fortranOrder);
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

  @Override
  public NpyLongArray asLongArray() {
    var longs = new long[data.length];
    for (int i = 0; i < data.length; i++) {
      longs[i] = (long) data[i];
    }
    return new NpyLongArray(copyShape(), longs, fortranOrder);
  }
}
