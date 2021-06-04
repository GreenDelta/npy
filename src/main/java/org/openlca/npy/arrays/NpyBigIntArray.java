package org.openlca.npy.arrays;

import java.math.BigInteger;

public class NpyBigIntArray extends AbstractNpyArray<BigInteger[]> {

  public NpyBigIntArray(int[] shape, BigInteger[] data, boolean fortranOrder) {
    super(shape, data, fortranOrder);
  }

  @Override
  public int size() {
    return data.length;
  }

  @Override
  public NpyBooleanArray asBooleanArray() {
    var booleans = new boolean[data.length];
    for (int i = 0; i < data.length; i++) {
      var val = data[i];
      if (val != null) {
        booleans[i] = val.longValueExact() != 0;
      }
    }
    return new NpyBooleanArray(copyShape(), booleans, fortranOrder);
  }

  @Override
  public NpyDoubleArray asDoubleArray() {
    var doubles = new double[data.length];
    for (int i = 0; i < data.length; i++) {
      var val = data[i];
      if (val != null) {
        doubles[i] = val.doubleValue();
      }
    }
    return new NpyDoubleArray(copyShape(), doubles, fortranOrder);
  }

  @Override
  public NpyFloatArray asFloatArray() {
    var floats = new float[data.length];
    for (int i = 0; i < data.length; i++) {
      var val = data[i];
      if (val != null) {
        floats[i] = (float) val.doubleValue();
      }
    }
    return new NpyFloatArray(copyShape(), floats, fortranOrder);
  }

  @Override
  public NpyIntArray asIntArray() {
    return null;
  }

  @Override
  public NpyLongArray asLongArray() {
    return null;
  }
}
