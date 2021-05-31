package org.openlca.npy;

import java.nio.ByteBuffer;

import org.openlca.npy.arrays.NpyArray;
import org.openlca.npy.arrays.NpyDoubleArray;
import org.openlca.npy.arrays.NpyFloatArray;
import org.openlca.npy.arrays.NpyIntArray;

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
      case i4:
        return new I4Builder(header);
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

  private static final class I4Builder extends NpyArrayBuilder {

    private final int[] data;

    private I4Builder(NpyHeader header) {
      super(header);
      this.data = new int[header.numberOfElements()];
    }

    @Override
    void fillNext(ByteBuffer buffer, int pos) {
      data[pos] = buffer.getInt();
    }

    @Override
    NpyIntArray build() {
      return new NpyIntArray(header.shape(), data, header.hasFortranOrder());
    }
  }

}
