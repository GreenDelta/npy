package org.openlca.npy;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.openlca.npy.dict.HeaderDictionary;

class Header {

  private final long dataOffset;
  private final HeaderDictionary dictionary;

  private Header(long dataOffset, HeaderDictionary dictionary) {
    this.dataOffset = dataOffset;
    this.dictionary = Objects.requireNonNull(dictionary);
  }

  @Override
  public String toString() {
    return dictionary.toString();
  }

  public HeaderDictionary dictionary() {
    return dictionary;
  }

  public long dataOffset() {
    return dataOffset;
  }

  static Header read(InputStream in) throws IOException {

    // read the version
    byte[] bytes = new byte[8];
    int n = in.read(bytes);
    if (n != 8)
      throw new UnsupportedFormatException("invalid NPY header");
    var version = Version.of(bytes);

    // read the header length; 2 bytes for version 1; 4 bytes for versions > 1
    int headerLength;
    long dataOffset;
    if (version.major == 1) {
      bytes = new byte[2];
      n = in.read(bytes);
      if (n != 2)
        throw new UnsupportedFormatException("invalid NPY header");
      headerLength = Unsigned.shortOf(bytes, ByteOrder.LITTLE_ENDIAN);
      dataOffset = 10 + headerLength;
    } else {
      bytes = new byte[4];
      n = in.read(bytes);
      if (n != 4)
        throw new UnsupportedFormatException("invalid NPY header");
      long len = Unsigned.intOf(bytes, ByteOrder.LITTLE_ENDIAN);
      dataOffset = 12 + len;
      headerLength = (int) len;
    }

    // read the header string
    bytes = new byte[headerLength];
    if (in.read(bytes) != headerLength)
      throw new UnsupportedFormatException("invalid NPY file");
    var header = new String(bytes, version.headerEncoding());
    return new Header(dataOffset, HeaderDictionary.parse(header));
  }

  static Header read(ReadableByteChannel channel) throws IOException {

    // read the version
    var buffer = ByteBuffer.allocate(8)
      .order(ByteOrder.LITTLE_ENDIAN);
    if (channel.read(buffer) < 8) {
      throw new UnsupportedFormatException("invalid NPY header");
    }
    buffer.flip();
    var version = Version.of(buffer.array());

    int headerLength;
    long dataOffset;
    buffer.position(0);
    if (version.major == 1) {
      buffer.limit(2);
      if (channel.read(buffer) != 2)
        throw new UnsupportedFormatException("invalid NPY header");
      buffer.flip();
      headerLength = Unsigned.shortOf(buffer);
      dataOffset = 10 + headerLength;
    } else {
      buffer.limit(4);
      if (channel.read(buffer) != 4)
        throw new UnsupportedFormatException("invalid NPY header");
      long len = Unsigned.intOf(buffer);
      dataOffset = 12 + len;
      headerLength = (int) len;
    }

    // read and parse the header
    buffer = ByteBuffer.allocate(headerLength);
    if (channel.read(buffer) != headerLength)
      throw new UnsupportedFormatException("invalid NPY file");
    var header = new String(buffer.array(), version.headerEncoding());
    return new Header(dataOffset, HeaderDictionary.parse(header));
  }

  /**
   * Contains the version information of the first bytes of an NPY file:
   * <ul>
   *   <li>the first 6 bytes are the magic string '\x93'</li>
   *   <li>bytes 7 and 8 contain the major and minor version</li>
   * </ul>
   */
  static class Version {

    final int major;
    final int minor;

    private Version(int major, int minor) {
      this.major = major;
      this.minor = minor;

    }

    /**
     * Reads the format version from the first 8 bytes of an NPY file. It checks
     * that the array starts with the magic string {@code '\x93NUMPY'} and that
     * the version is in a supported range. If this is not the case, it throws
     * an {@code UnsupportedFormatException}.
     *
     * @param bytes at least, the first 8 bytes of an NPY file
     * @return the NPY version of that file
     */
    static Version of(byte[] bytes) {
      if (bytes.length < 8)
        throw new UnsupportedFormatException("invalid NPY header");
      if (Unsigned.byteOf(bytes[0]) != 0x93)
        throw new UnsupportedFormatException("invalid NPY header");
      var numpy = new String(bytes, 1, 5);
      if (!numpy.equals("NUMPY"))
        throw new UnsupportedFormatException("invalid NPY header");

      int major = Unsigned.byteOf(bytes[6]);
      int minor = Unsigned.byteOf(bytes[7]);
      if (major != 1 && major != 2 && major != 3)
        throw new UnsupportedFormatException(
          "unsupported NPY version: " + major);
      return new Version(major, minor);
    }

    Charset headerEncoding() {
      return major >= 3
        ? StandardCharsets.UTF_8
        : StandardCharsets.US_ASCII;
    }
  }

}
