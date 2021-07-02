package org.openlca.npy.arrays;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
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
  public NpyBooleanArray asBooleanArray() {

    return null;
  }

  @Override
  public NpyCharArray asCharArray() {
    return this;
  }

  @Override
  public NpyByteArray asByteArray() {
    CharBuffer.wrap(data).codePoints().
    return null;
  }

  @Override
  public NpyDoubleArray asDoubleArray() {
    return null;
  }

  @Override
  public NpyFloatArray asFloatArray() {
    return null;
  }

  @Override
  public NpyIntArray asIntArray() {
    return null;
  }

  @Override
  public NpyLongArray asLongArray() {
    return null;
  }

  @Override
  public NpyShortArray asShortArray() {
    return null;
  }
}
