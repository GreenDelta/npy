package org.openlca.npy.arrays;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import org.openlca.npy.DataType;

public final class NpyBigIntArray extends AbstractNpyArray<BigInteger[]> {

  public NpyBigIntArray(int[] shape, BigInteger[] data, boolean fortranOrder) {
    super(shape, data, fortranOrder);
  }

  @Override
  public DataType dataType() {
    return DataType.u8;
  }

  @Override
  public int size() {
    return data.length;
  }

  @Override
  public void writeElementTo(int i, ByteBuffer buffer) {
    var value = data[i];
    if (value == null) {
      buffer.putLong(0);
    } else {
      buffer.putLong(value.longValueExact());
    }
  }

  @Override
  public boolean isBigIntegerArray() {
    return true;
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
  public NpyByteArray asByteArray() {
    var bytes = new byte[data.length];
    for (int i = 0; i < data.length; i++) {
      var val = data[i];
      if (val != null) {
        bytes[i] = (byte) val.intValueExact();
      }
    }
    return new NpyByteArray(copyShape(), bytes, fortranOrder);
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
    var ints = new int[data.length];
    for (int i = 0; i < data.length; i++) {
      var val = data[i];
      if (val != null) {
        ints[i] = val.intValueExact();
      }
    }
    return new NpyIntArray(copyShape(), ints, fortranOrder);
  }

  @Override
  public NpyLongArray asLongArray() {
    var longs = new long[data.length];
    for (int i = 0; i < data.length; i++) {
      var val = data[i];
      if (val != null) {
        longs[i] = val.longValueExact();
      }
    }
    return new NpyLongArray(copyShape(), longs, fortranOrder);
  }

  @Override
  public NpyShortArray asShortArray() {
    var shorts = new short[data.length];
    for (int i = 0; i < data.length; i++) {
      var val = data[i];
      if (val != null) {
        shorts[i] = (short) val.intValueExact();
      }
    }
    return new NpyShortArray(copyShape(), shorts, fortranOrder);
  }
}
