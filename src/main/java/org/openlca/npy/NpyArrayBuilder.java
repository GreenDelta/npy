package org.openlca.npy;

import java.nio.ByteBuffer;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.openlca.npy.arrays.NpyArray;
import org.openlca.npy.arrays.NpyDoubleArray;
import org.openlca.npy.arrays.NpyFloatArray;
import org.openlca.npy.arrays.NpyIntArray;
import org.openlca.npy.arrays.NpyLongArray;

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
      case f8:
        return new F8Builder(header);
      case f4:
        return new F4Builder(header);
      case i8:
        return new LongBuilder(header, ByteBuffer::getLong);
      case u2:
        return new IntBuilder(header, Unsigned::u2ToSigned);
      case u4:
        return new LongBuilder(header, Unsigned::u4ToSigned);
      case i4:
        return new IntBuilder(header, ByteBuffer::getInt);
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

  private static final class F8Builder extends NpyArrayBuilder {

    private final double[] data;

    private F8Builder(NpyHeader header) {
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

  private static final class F4Builder extends NpyArrayBuilder {

    private final float[] data;

    private F4Builder(NpyHeader header) {
      super(header);
      this.data = new float[header.numberOfElements()];
    }

    @Override
    void fillNext(ByteBuffer buffer, int pos) {
      data[pos] = buffer.getFloat();
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

}
