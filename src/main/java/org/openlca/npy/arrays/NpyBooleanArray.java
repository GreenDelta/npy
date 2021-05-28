
package org.openlca.npy.arrays;

public final class NpyBooleanArray extends AbstractNpyArray<boolean[]> {

  public NpyBooleanArray(int[] shape, boolean[] data, boolean fortranOrder) {
    super(shape, data, fortranOrder);
  }

  @Override
  public int size() {
    return data.length;
  }
  
    /*
    @Override
    public boolean isBooleanArray() {
      return true;
    }
  
    @Override
    public NpyBooleanArray asBooleanArray() {
      return this;
    }
    */

  @Override
  public NpyDoubleArray asDoubleArray() {
    var doubles = new double[data.length];
    for (int i = 0; i < data.length; i++) {
      doubles[i] = data[i] ? 1 : 0;
    }
    return new NpyDoubleArray(copyShape(), doubles, fortranOrder);
  }

  @Override
  public NpyFloatArray asFloatArray() {
    var floats = new float[data.length];
    for (int i = 0; i < data.length; i++) {
      floats[i] = data[i] ? 1f : 0f;
    }
    return new NpyFloatArray(copyShape(), floats, fortranOrder);
  }
}
  
