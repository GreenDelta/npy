
package org.openlca.npy.arrays;

public final class NpyBooleanArray extends AbstractNpyArray<boolean[]> {

  public NpyBooleanArray(int[] shape, boolean[] data, boolean fortranOrder) {
    super(shape, data, fortranOrder);
  }

  @Override
  public int size() {
    return data.length;
  }

  @Override
  public boolean isBooleanArray() {
    return true;
  }

  @Override
  public NpyBooleanArray asBooleanArray() {
    return this;
  }

  @Override
  public NpyDoubleArray asDoubleArray() {
    var doubles = new double[data.length];
    for (int i = 0; i < data.length; i++) {
      if (data[i]) {
        doubles[i] = 1d;
      }
    }
    return new NpyDoubleArray(copyShape(), doubles, fortranOrder);
  }

  @Override
  public NpyFloatArray asFloatArray() {
    var floats = new float[data.length];
    for (int i = 0; i < data.length; i++) {
      if (data[i]) {
        floats[i] = 1f;
      }
    }
    return new NpyFloatArray(copyShape(), floats, fortranOrder);
  }

  @Override
  public NpyIntArray asIntArray() {
    var ints = new int[data.length];
    for (int i = 0; i < data.length; i++) {
      if (data[i]) {
        ints[i] = 1;
      }
    }
    return new NpyIntArray(copyShape(), ints, fortranOrder);
  }

  @Override
  public NpyLongArray asLongArray() {
    var longs = new long[data.length];
    for (int i = 0; i < data.length; i++) {
      if (data[i]) {
        longs[i] = 1L;
      }
    }
    return new NpyLongArray(copyShape(), longs, fortranOrder);
  }
}
  
