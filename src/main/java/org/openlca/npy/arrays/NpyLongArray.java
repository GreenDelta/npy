package org.openlca.npy.arrays;

import java.nio.ByteBuffer;

import org.openlca.npy.NpyDataType;

public final class NpyLongArray extends AbstractNpyArray<long[]> {

  public NpyLongArray(int[] shape, long[] data, boolean fortranOrder) {
    super(shape, data, fortranOrder);
  }

  @Override
  public NpyDataType dataType() {
    return NpyDataType.i8;
  }

  @Override
  public int size() {
    return data.length;
  }

  @Override
  public void writeElementTo(int i, ByteBuffer buffer) {
    buffer.putLong(data[i]);
  }

  @Override
  public boolean isLongArray() {
    return true;
  }

  @Override
  public NpyLongArray asLongArray() {
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
  public NpyIntArray asIntArray() {
    var ints = new int[data.length];
    for (int i = 0; i < data.length; i++) {
      ints[i] = (int) data[i];
    }
    return new NpyIntArray(copyShape(), ints, fortranOrder);
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
  
