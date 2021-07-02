package org.openlca.npy.arrays;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;

import org.openlca.npy.NpyDataType;

public class NpyCharArray extends AbstractNpyArray<char[]> {

  private NpyDataType type;

  public NpyCharArray(int[] shape, char[] data) {
    super(shape, data, false);
  }

  public NpyCharArray(int[] shape, char[] data, boolean fortranOrder) {
    super(shape, data, fortranOrder);
  }

  public static NpyCharArray of(String s) {
    var chars = s.toCharArray();
    return new NpyCharArray(new int[0], chars);
  }

  @Override
  public NpyDataType dataType() {
    if (type != null)
      return type;
    var isAscii = StandardCharsets.US_ASCII
      .newEncoder()
      .canEncode(CharBuffer.wrap(data));
    type = isAscii
      ? NpyDataType.S
      : NpyDataType.U;
    return type;
  }

  @Override
  public void writeElementTo(int i, ByteBuffer buffer) {
    if (dataType() == NpyDataType.S) {
      buffer.put((byte) data[i]);
    } else {
      buffer.putInt(data[i]);
    }
  }

  @Override
  public int size() {
    return data.length;
  }

  @Override
  public boolean isCharArray() {
    return true;
  }

  @Override
  public NpyBooleanArray asBooleanArray() {
    var booleans = new boolean[data.length];
    for (int i = 0; i < data.length; i++) {
      booleans[i] = data[i] != 0;
    }
    return new NpyBooleanArray(copyShape(), booleans, fortranOrder);
  }

  @Override
  public NpyByteArray asByteArray() {
    var bytes = String.valueOf(data)
      .getBytes(StandardCharsets.UTF_8);
    return new NpyByteArray(new int[0], bytes, false);
  }

  @Override
  public NpyCharArray asCharArray() {
    return this;
  }

  @Override
  public NpyDoubleArray asDoubleArray() {
    return asIntArray().asDoubleArray();
  }

  @Override
  public NpyFloatArray asFloatArray() {
    return asIntArray().asFloatArray();
  }

  @Override
  public NpyIntArray asIntArray() {
    var buffer = IntBuffer.allocate(data.length);
    int pos = 0;
    while (pos < data.length) {
      int codePoint = Character.codePointAt(data, pos);
      buffer.put(codePoint);
      pos += Character.charCount(codePoint);
    }

    int[] ints;
    if (buffer.remaining() == 0) {
      ints = buffer.array();
    } else {
      buffer.flip();
      ints = new int[buffer.limit()];
      buffer.get(ints, 0, buffer.limit());
    }
    return new NpyIntArray(copyShape(), ints, fortranOrder);
  }

  @Override
  public NpyLongArray asLongArray() {
    return asIntArray().asLongArray();
  }

  @Override
  public NpyShortArray asShortArray() {
    return asIntArray().asShortArray();
  }

  @Override
  public String toString() {
    return String.valueOf(data);
  }
}
