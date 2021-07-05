package org.openlca.npy;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.openlca.npy.arrays.NpyArray;
import org.openlca.npy.arrays.NpyBigIntArray;
import org.openlca.npy.arrays.NpyBooleanArray;
import org.openlca.npy.arrays.NpyByteArray;
import org.openlca.npy.arrays.NpyCharArray;
import org.openlca.npy.arrays.NpyDoubleArray;
import org.openlca.npy.arrays.NpyFloatArray;
import org.openlca.npy.arrays.NpyIntArray;
import org.openlca.npy.arrays.NpyLongArray;
import org.openlca.npy.arrays.NpyShortArray;
import org.openlca.npy.dict.NpyHeaderDict;

abstract class NpyArrayBuilder {

  protected final NpyHeaderDict dict;
  protected final int elementCount;
  private final int elementSize;
  private int pos;

  private NpyArrayBuilder(NpyHeaderDict dict) {
    this.dict = dict;
    var type = dict.dataType();
    this.elementCount = type == NpyDataType.S || type == NpyDataType.U
      ? dict.typeSize()
      : dict.numberOfElements();
    this.elementSize = type == NpyDataType.S ? 1
      : type == NpyDataType.U ? 4 : type.size();
    this.pos = 0;
  }

  static NpyArrayBuilder allocate(NpyHeaderDict dict) throws NpyFormatException {
    switch (dict.dataType()) {
      case bool:
        return new BooleanBuilder(dict);
      case f2:
        return new FloatBuilder(dict, Util::f2ToFloat);
      case f4:
        return new FloatBuilder(dict, ByteBuffer::getFloat);
      case f8:
        return new DoubleBuilder(dict);
      case i1:
        return new ByteBuilder(dict);
      case i2:
        return new ShortBuilder(dict, ByteBuffer::getShort);
      case i4:
        return new IntBuilder(dict, ByteBuffer::getInt);
      case i8:
        return new LongBuilder(dict, ByteBuffer::getLong);
      case u1:
        return new ShortBuilder(dict, Util::u1ToShort);
      case u2:
        return new IntBuilder(dict, Util::u2ToInt);
      case u4:
        return new LongBuilder(dict, Util::u4ToLong);
      case u8:
        return new BigIntBuilder(dict);
      case S:
        return new AsciiBuilder(dict);
      case U:
        return new UnicodeBuilder(dict);
      default:
        throw new NpyFormatException(
          "unsupported data type: " + dict.dataType());
    }
  }

  final void readAllFrom(ByteBuffer buffer) {
    while (pos != elementCount && buffer.remaining() >= elementSize) {
      fillNext(buffer, pos);
      pos++;
    }
  }

  abstract void fillNext(ByteBuffer buffer, int pos);

  abstract NpyArray<?> build();

  private static final class BooleanBuilder extends NpyArrayBuilder {

    private final boolean[] data;

    private BooleanBuilder(NpyHeaderDict dict) {
      super(dict);
      this.data = new boolean[elementCount];
    }

    @Override
    void fillNext(ByteBuffer buffer, int pos) {
      data[pos] = buffer.get() != 0;
    }

    @Override
    NpyBooleanArray build() {
      return new NpyBooleanArray(dict.shape(), data, dict.hasFortranOrder());
    }
  }

  private static final class ByteBuilder extends NpyArrayBuilder {

    private final byte[] data;

    private ByteBuilder(NpyHeaderDict dict) {
      super(dict);
      this.data = new byte[elementCount];
    }

    @Override
    void fillNext(ByteBuffer buffer, int pos) {
      data[pos] = buffer.get();
    }

    @Override
    NpyByteArray build() {
      return new NpyByteArray(dict.shape(), data, dict.hasFortranOrder());
    }
  }

  private static final class DoubleBuilder extends NpyArrayBuilder {

    private final double[] data;

    private DoubleBuilder(NpyHeaderDict dict) {
      super(dict);
      this.data = new double[elementCount];
    }

    @Override
    void fillNext(ByteBuffer buffer, int pos) {
      data[pos] = buffer.getDouble();
    }

    @Override
    NpyDoubleArray build() {
      return new NpyDoubleArray(dict.shape(), data, dict.hasFortranOrder());
    }
  }

  private static final class FloatBuilder extends NpyArrayBuilder {

    private final float[] data;
    private final ToFloatFunction<ByteBuffer> fn;

    private FloatBuilder(NpyHeaderDict dict, ToFloatFunction<ByteBuffer> fn) {
      super(dict);
      this.data = new float[elementCount];
      this.fn = fn;
    }

    @Override
    void fillNext(ByteBuffer buffer, int pos) {
      data[pos] = fn.applyAsFloat(buffer);
    }

    @Override
    NpyFloatArray build() {
      return new NpyFloatArray(dict.shape(), data, dict.hasFortranOrder());
    }
  }

  private static final class IntBuilder extends NpyArrayBuilder {

    private final int[] data;
    private final ToIntFunction<ByteBuffer> fn;

    private IntBuilder(NpyHeaderDict dict, ToIntFunction<ByteBuffer> fn) {
      super(dict);
      this.data = new int[elementCount];
      this.fn = fn;
    }

    @Override
    void fillNext(ByteBuffer buffer, int pos) {
      data[pos] = fn.applyAsInt(buffer);
    }

    @Override
    NpyIntArray build() {
      return new NpyIntArray(dict.shape(), data, dict.hasFortranOrder());
    }
  }

  private static final class ShortBuilder extends NpyArrayBuilder {

    private final short[] data;
    private final ToShortFunction<ByteBuffer> fn;

    private ShortBuilder(NpyHeaderDict dict, ToShortFunction<ByteBuffer> fn) {
      super(dict);
      this.data = new short[elementCount];
      this.fn = fn;
    }

    @Override
    void fillNext(ByteBuffer buffer, int pos) {
      data[pos] = fn.applyAsShort(buffer);
    }

    @Override
    NpyShortArray build() {
      return new NpyShortArray(dict.shape(), data, dict.hasFortranOrder());
    }
  }

  private static final class LongBuilder extends NpyArrayBuilder {

    private final long[] data;
    private final ToLongFunction<ByteBuffer> fn;

    private LongBuilder(NpyHeaderDict dict, ToLongFunction<ByteBuffer> fn) {
      super(dict);
      this.data = new long[elementCount];
      this.fn = fn;
    }

    @Override
    void fillNext(ByteBuffer buffer, int pos) {
      data[pos] = fn.applyAsLong(buffer);
    }

    @Override
    NpyLongArray build() {
      return new NpyLongArray(dict.shape(), data, dict.hasFortranOrder());
    }
  }

  private static final class BigIntBuilder extends NpyArrayBuilder {

    private final BigInteger[] data;

    private BigIntBuilder(NpyHeaderDict dict) {
      super(dict);
      this.data = new BigInteger[elementCount];
    }

    @Override
    void fillNext(ByteBuffer buffer, int pos) {
      data[pos] = Util.u8ToBigInteger(buffer);
    }

    @Override
    NpyBigIntArray build() {
      return new NpyBigIntArray(dict.shape(), data, dict.hasFortranOrder());
    }
  }

  private static final class AsciiBuilder extends NpyArrayBuilder {

    private final CharBuffer chars;
    private boolean terminated = false;

    private AsciiBuilder(NpyHeaderDict dict) {
      super(dict);
      this.chars = CharBuffer.allocate(elementCount);
    }

    @Override
    void fillNext(ByteBuffer buffer, int pos) {
      if (terminated)
        return;
      char next = (char)buffer.get();
      if (next == 0) {
        terminated = true;
        return;
      }
      chars.put(next);
    }

    @Override
    NpyCharArray build() {
      char[] data;
      if (chars.remaining() == 0) {
        data = chars.array();
      } else {
        chars.flip();
        data = new char[chars.limit()];
        chars.get(data, 0, chars.limit());
      }

      return new NpyCharArray(dict.shape(), data, dict.hasFortranOrder());
    }
  }

  private static final class UnicodeBuilder extends NpyArrayBuilder {

    private final int[] data;

    private UnicodeBuilder(NpyHeaderDict dict) {
      super(dict);
      this.data = new int[elementCount];
    }

    @Override
    void fillNext(ByteBuffer buffer, int pos) {
      data[pos] = buffer.getInt();
    }

    @Override
    NpyCharArray build() {
      var ints = new NpyIntArray(dict.shape(), data, dict.hasFortranOrder());
      return ints.asCharArray();
    }
  }
}
