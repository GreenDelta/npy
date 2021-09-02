package org.openlca.npy.arrays;

import java.nio.ByteBuffer;

import org.openlca.npy.NpyDataType;

public final class NpyByteArray extends AbstractNpyArray<byte[]> {

  public NpyByteArray(int[] shape, byte[] data, boolean fortranOrder) {
    super(shape, data, fortranOrder);
  }

  public static NpyByteArray vectorOf(byte[] data) {
    return new NpyByteArray(new int[] {data.length}, data, false);
  }

  @Override
  public NpyDataType dataType() {
    return NpyDataType.i1;
  }

  @Override
  public int size() {
    return data.length;
  }

  @Override
  public void writeElementTo(int i, ByteBuffer buffer) {
    buffer.put(data[i]);
  }

  @Override
  public boolean isByteArray() {
    return true;
  }

  @Override
  public NpyByteArray asByteArray() {
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
      shorts[i] = data[i];
    }
    return new NpyShortArray(copyShape(), shorts, fortranOrder);
  }
}
  
