package org.openlca.npy.arrays;

public final class NpyShortArray extends AbstractNpyArray<short[]> {

  public NpyShortArray(int[] shape, short[] data, boolean fortranOrder) {
    super(shape, data, fortranOrder);
  }

  @Override
  public int size() {
    return data.length;
  }
  
    /*
    @Override
    public boolean isShortArray() {
      return true;
    }
  
    @Override
    public NpyShortArray asShortArray() {
      return this;
    }
    */

  @Override
  public NpyDoubleArray asDoubleArray() {
    var doubles = new double[data.length];
    for (int i = 0; i < data.length; i++) {
      doubles[i] = data[i];
    }
    return new NpyDoubleArray(copyShape(), doubles, fortranOrder);
  }

  @Override
  public NpyFloatArray asFloatArray() {
    var floats = new float[data.length];
    for (int i = 0; i < data.length; i++) {
      floats[i] = data[i];
    }
    return new NpyFloatArray(copyShape(), floats, fortranOrder);
  }

  @Override
  public NpyIntArray asIntArray() {
    var ints = new int[data.length];
    for (int i = 0; i < data.length; i++) {
      ints[i] = data[i];
    }
    return new NpyIntArray(copyShape(), ints, fortranOrder);
  }
}
  
