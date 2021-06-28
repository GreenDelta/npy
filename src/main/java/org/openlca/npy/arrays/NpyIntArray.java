package org.openlca.npy.arrays;

import java.nio.ByteBuffer;

import org.openlca.npy.NpyDataTypes;

public final class NpyIntArray extends AbstractNpyArray<int[]> {

  public NpyIntArray(int[] shape, int[] data, boolean fortranOrder) {
    super(shape, data, fortranOrder);
  }

  @Override
  public NpyDataTypes dataType() {
    return NpyDataTypes.i4;
  }

  @Override
  public int size() {
    return data.length;
  }

  @Override
  public void writeElementTo(int i, ByteBuffer buffer) {
    buffer.putInt(data[i]);
  }

  @Override
  public boolean isIntArray() {
    return true;
  }

  @Override
  public NpyIntArray asIntArray() {
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
  public NpyByteArray asByteArray() {
    var bytes = new byte[data.length];
    for (int i = 0; i < data.length; i++) {
      bytes[i] = (byte) data[i];
    }
    return new NpyByteArray(copyShape(), bytes, fortranOrder);
  }

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
      floats[i] = (float) data[i];
    }
    return new NpyFloatArray(copyShape(), floats, fortranOrder);
  }

  @Override
  public NpyLongArray asLongArray() {
    var longs = new long[data.length];
    for (int i = 0; i < data.length; i++) {
      longs[i] = data[i];
    }
    return new NpyLongArray(copyShape(), longs, fortranOrder);
  }

  @Override
  public NpyShortArray asShortArray() {
    var shorts = new short[data.length];
    for (int i = 0; i < data.length; i++) {
      shorts[i] = (short) data[i];
    }
    return new NpyShortArray(copyShape(), shorts, fortranOrder);
  }
}
  
