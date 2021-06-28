package org.openlca.npy;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;

import org.openlca.npy.dict.HeaderDictionary;

/**
 * The NPY header contains the meta data of the stored array and the NPY file.
 */
public final class NpyHeader {

  private final long dataOffset;
  private final HeaderDictionary dictionary;

  private NpyHeader(long dataOffset, HeaderDictionary dictionary) {
    this.dataOffset = dataOffset;
    this.dictionary = Objects.requireNonNull(dictionary);
  }

  @Override
  public String toString() {
    return dictionary.toString();
  }

  public NpyDataTypes dataType() {
    return dictionary.dataType();
  }

  /**
   * Returns the 0-based position from where the data start in the NPY file.
   */
  public long dataOffset() {
    return dataOffset;
  }

  /**
   * Returns the size of the stored array in number of bytes. That is the
   * number of elements of the stored array times the size of the data type in
   * bytes.
   *
   * @return the size of the stored array in bytes
   */
  public long dataSize() {
    long elemCount = numberOfElements();
    long typeSize = dataType().size();
    return elemCount * typeSize;
  }

  /**
   * Returns {@code true} when the array is stored in Fortran order.
   */
  public boolean hasFortranOrder() {
    return dictionary.hasFortranOrder();
  }

  /**
   * Returns the shape of the stored array. Note that this returns a new
   * allocated array each time you call this method.
   *
   * @return the shape of the stored array
   */
  public int[] shape() {
    int n = dictionary.dimensions();
    int[] shape = new int[n];
    for (int i = 0; i < shape.length; i++) {
      shape[i] = dictionary.sizeOfDimension(i);
    }
    return shape;
  }

  public ByteOrder byteOrder() {
    return dictionary.byteOrder();
  }

  /**
   * Returns the number of elements that are stored in the array.
   *
   * @return the number of elements which is the product of all dimension sizes.
   */
  public int numberOfElements() {
    int count = 1;
    int n = dictionary.dimensions();
    for (int i = 0; i < n; i++) {
      count *= dictionary.sizeOfDimension(i);
    }
    return count;
  }

  public String property(String key) {
    return dictionary.otherProperties().get(key);
  }

  public static NpyHeader read(InputStream in)
    throws IOException, NpyFormatException {

    // read the version
    byte[] bytes = new byte[8];
    int n = in.read(bytes);
    if (n != 8)
      throw new NpyFormatException("invalid NPY header");
    var version = NpyVersion.of(bytes);

    // read the header length; 2 bytes for version 1; 4 bytes for versions > 1
    int headerLength;
    long dataOffset;
    if (version.major == 1) {
      bytes = new byte[2];
      n = in.read(bytes);
      if (n != 2)
        throw new NpyFormatException("invalid NPY header");
      headerLength = Util.u2ToInt(bytes, ByteOrder.LITTLE_ENDIAN);
      dataOffset = 10 + headerLength;
    } else {
      bytes = new byte[4];
      n = in.read(bytes);
      if (n != 4)
        throw new NpyFormatException("invalid NPY header");
      long len = Util.u4ToLong(bytes, ByteOrder.LITTLE_ENDIAN);
      dataOffset = 12 + len;
      headerLength = (int) len;
    }

    // read the header string
    bytes = new byte[headerLength];
    if (in.read(bytes) != headerLength)
      throw new NpyFormatException("invalid NPY file");
    var header = new String(bytes, version.headerEncoding());
    return new NpyHeader(dataOffset, HeaderDictionary.parse(header));
  }

  static NpyHeader read(ReadableByteChannel channel) throws IOException {

    // read the version
    var buffer = ByteBuffer.allocate(8)
      .order(ByteOrder.LITTLE_ENDIAN);
    if (channel.read(buffer) < 8) {
      throw new NpyFormatException("invalid NPY header");
    }
    buffer.flip();
    var version = NpyVersion.of(buffer.array());

    int headerLength;
    long dataOffset;
    buffer.position(0);
    if (version.major == 1) {
      buffer.limit(2);
      if (channel.read(buffer) != 2)
        throw new NpyFormatException("invalid NPY header");
      buffer.flip();
      headerLength = Util.u2ToInt(buffer);
      dataOffset = 10 + headerLength;
    } else {
      buffer.limit(4);
      if (channel.read(buffer) != 4)
        throw new NpyFormatException("invalid NPY header");
      long len = Util.u4ToLong(buffer);
      dataOffset = 12 + len;
      headerLength = (int) len;
    }

    // read and parse the header
    buffer = ByteBuffer.allocate(headerLength);
    if (channel.read(buffer) != headerLength)
      throw new NpyFormatException("invalid NPY file");
    var header = new String(buffer.array(), version.headerEncoding());
    return new NpyHeader(dataOffset, HeaderDictionary.parse(header));
  }

}
