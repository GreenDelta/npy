package org.openlca.npy;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.openlca.npy.arrays.NpyArray;
import org.openlca.npy.arrays.NpyBigIntArray;
import org.openlca.npy.arrays.NpyBooleanArray;
import org.openlca.npy.arrays.NpyByteArray;
import org.openlca.npy.arrays.NpyDoubleArray;
import org.openlca.npy.arrays.NpyFloatArray;
import org.openlca.npy.arrays.NpyIntArray;
import org.openlca.npy.arrays.NpyLongArray;
import org.openlca.npy.arrays.NpyShortArray;

abstract class NpyArrayBuilder {

  protected final NpyHeader header;
  private final int size;
  private final int typeSize;
  private int pos;

  private NpyArrayBuilder(NpyHeader header) {
    this.header = header;
    this.size = header.numberOfElements();
    this.typeSize = header.dataType().size();
    this.pos = 0;
  }

  static NpyArrayBuilder allocate(NpyHeader header) throws NpyFormatException {
    switch (header.dataType()) {
      case bool:
        return new BooleanBuilder(header);
      case f2:
        return new FloatBuilder(header, Util::f2ToFloat);
      case f4:
        return new FloatBuilder(header, ByteBuffer::getFloat);
      case f8:
        return new DoubleBuilder(header);
      case i1:
        return new ByteBuilder(header);
      case i2:
        return new ShortBuilder(header, ByteBuffer::getShort);
      case i4:
        return new IntBuilder(header, ByteBuffer::getInt);
      case i8:
        return new LongBuilder(header, ByteBuffer::getLong);
      case u1:
        return new ShortBuilder(header, Util::u1ToShort);
      case u2:
        return new IntBuilder(header, Util::u2ToInt);
      case u4:
        return new LongBuilder(header, Util::u4ToLong);
      case u8:
        return new BigIntBuilder(header);
      default:
        throw new NpyFormatException(
          "unsupported data type: " + header.dataType());
    }
  }

  final void next(ByteBuffer buffer) {
    while (pos != size && buffer.remaining() >= typeSize) {
      fillNext(buffer, pos);
      pos++;
    }
  }

  abstract void fillNext(ByteBuffer buffer, int pos);

  abstract NpyArray<?> build();

  private static final class BooleanBuilder extends NpyArrayBuilder {

    private final boolean[] data;

    private BooleanBuilder(NpyHeader header) {
      super(header);
      this.data = new boolean[header.numberOfElements()];
    }

    @Override
    void fillNext(ByteBuffer buffer, int pos) {
      data[pos] = buffer.get() != 0;
    }

    @Override
    NpyBooleanArray build() {
      return new NpyBooleanArray(header.shape(), data, header.hasFortranOrder());
    }
  }

  private static final class ByteBuilder extends NpyArrayBuilder {

    private final byte[] data;

    private ByteBuilder(NpyHeader header) {
      super(header);
      this.data = new byte[header.numberOfElements()];
    }

    @Override
    void fillNext(ByteBuffer buffer, int pos) {
      data[pos] = buffer.get();
    }

    @Override
    NpyByteArray build() {
      return new NpyByteArray(header.shape(), data, header.hasFortranOrder());
    }
  }

  private static final class DoubleBuilder extends NpyArrayBuilder {

    private final double[] data;

    private DoubleBuilder(NpyHeader header) {
      super(header);
      this.data = new double[header.numberOfElements()];
    }

    @Override
    void fillNext(ByteBuffer buffer, int pos) {
      data[pos] = buffer.getDouble();
    }

    @Override
    NpyDoubleArray build() {
      return new NpyDoubleArray(header.shape(), data, header.hasFortranOrder());
    }
  }

  private static final class FloatBuilder extends NpyArrayBuilder {

    private final float[] data;
    private final ToFloatFunction<ByteBuffer> fn;

    private FloatBuilder(NpyHeader header, ToFloatFunction<ByteBuffer> fn) {
      super(header);
      this.data = new float[header.numberOfElements()];
      this.fn = fn;
    }

    @Override
    void fillNext(ByteBuffer buffer, int pos) {
      data[pos] = fn.applyAsFloat(buffer);
    }

    @Override
    NpyFloatArray build() {
      return new NpyFloatArray(header.shape(), data, header.hasFortranOrder());
    }
  }

  private static final class IntBuilder extends NpyArrayBuilder {

    private final int[] data;
    private final ToIntFunction<ByteBuffer> fn;

    private IntBuilder(NpyHeader header, ToIntFunction<ByteBuffer> fn) {
      super(header);
      this.data = new int[header.numberOfElements()];
      this.fn = fn;
    }

    @Override
    void fillNext(ByteBuffer buffer, int pos) {
      data[pos] = fn.applyAsInt(buffer);
    }

    @Override
    NpyIntArray build() {
      return new NpyIntArray(header.shape(), data, header.hasFortranOrder());
    }
  }

  private static final class ShortBuilder extends NpyArrayBuilder {

    private final short[] data;
    private final ToShortFunction<ByteBuffer> fn;

    private ShortBuilder(NpyHeader header, ToShortFunction<ByteBuffer> fn) {
      super(header);
      this.data = new short[header.numberOfElements()];
      this.fn = fn;
    }

    @Override
    void fillNext(ByteBuffer buffer, int pos) {
      data[pos] = fn.applyAsShort(buffer);
    }

    @Override
    NpyShortArray build() {
      return new NpyShortArray(header.shape(), data, header.hasFortranOrder());
    }
  }

  private static final class LongBuilder extends NpyArrayBuilder {

    private final long[] data;
    private final ToLongFunction<ByteBuffer> fn;

    private LongBuilder(NpyHeader header, ToLongFunction<ByteBuffer> fn) {
      super(header);
      this.data = new long[header.numberOfElements()];
      this.fn = fn;
    }

    @Override
    void fillNext(ByteBuffer buffer, int pos) {
      data[pos] = fn.applyAsLong(buffer);
    }

    @Override
    NpyLongArray build() {
      return new NpyLongArray(header.shape(), data, header.hasFortranOrder());
    }
  }

  private static final class BigIntBuilder extends NpyArrayBuilder {

    private final BigInteger[] data;

    private BigIntBuilder(NpyHeader header) {
      super(header);
      this.data = new BigInteger[header.numberOfElements()];
    }

    @Override
    void fillNext(ByteBuffer buffer, int pos) {
      data[pos] = Util.u8ToBigInteger(buffer);
    }

    @Override
    NpyBigIntArray build() {
      return new NpyBigIntArray(header.shape(), data, header.hasFortranOrder());
    }
  }
  
}
