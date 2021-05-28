package org.openlca.npy.arrays;

public final class NpyByteArray extends AbstractNpyArray<byte[]> {

  public NpyByteArray(int[] shape, byte[] data, boolean fortranOrder) {
    super(shape, data, fortranOrder);
  }

  @Override
  public int size() {
    return data.length;
  }
  
    /*
    @Override
    public boolean isByteArray() {
      return true;
    }
  
    @Override
    public NpyByteArray asByteArray() {
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
}
  
