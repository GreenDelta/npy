package org.openlca.npy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;

import org.openlca.npy.arrays.NpyArray;
import org.openlca.npy.dict.HeaderDictionary;

public class Npy {

  /**
   * Load the content of the given file into NPY array instance.
   *
   * @param file the NPY file to read
   * @return the mapped NPY array
   * @throws NpyFormatException if the NPY format is invalid or unsupported
   * @throws RuntimeException   IO exceptions are wrapped in runtime exceptions
   */
  public static NpyArray<?> read(File file) {
    try (var f = new RandomAccessFile(file, "r");
         var channel = f.getChannel()) {
      var header = NpyHeader.read(channel);
      return ChannelReader.read(channel, header);
    } catch (IOException e) {
      throw new RuntimeException("failed to read file: " + file, e);
    }
  }

  /**
   * Reads a string from the given NPY file. The NumPy type of the stored data
   * must be {@link NpyDataTypes#S} or {@link NpyDataTypes#U}, otherwise an
   * {@link NpyFormatException} is thrown.
   *
   * @param file the NPY file with the stored string
   * @return the stored string
   */
  public static String readString(File file) {
    try (var f = new RandomAccessFile(file, "r");
         var channel = f.getChannel()) {

      // read the header and check that the file contains a string type
      var header = NpyHeader.read(channel);
      var type = header.dataType();
      if (type != NpyDataTypes.S && type != NpyDataTypes.U)
        throw new NpyFormatException(
          "file '" + file + "' does not contain an NPY string type: "
          + header.dataType());

      // read the n data bytes
      int n = (int) (channel.size() - header.dataOffset());
      if (n <= 0)
        return "";
      var buffer = ByteBuffer.allocate(n);
      n = channel.read(buffer);
      if (n <= 0)
        return "";

      var array = buffer.array();

      // exclude the last byte for 0-terminated strings
      if (type == NpyDataTypes.S && array[n - 1] == 0) {
        if (n == 1)
          return "";
        n -= 1;
      }

      return new String(array, 0, n, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("failed to read string from file: " + file, e);
    }
  }

  public static String readString(ReadableByteChannel channel) {
    try {

      // read the header and check the type
      var header = NpyHeader.read(channel);
      var type = header.dataType();
      if (type != NpyDataTypes.S && type != NpyDataTypes.U)
        throw new NpyFormatException(
          type + " is not a supported string type");

      // read the data from the channel
      int buffSize = 1024;
      var buff = ByteBuffer.allocate(buffSize);
      var bout = new ByteArrayOutputStream();
      while (true) {
        int n = channel.read(buff);
        if (n <= 0)
          break;
        bout.write(buff.array(), 0, n);
        if (n < buffSize)
          break;
        buff.flip();
        buff.clear();
      }

      var bytes = bout.toByteArray();
      if (bytes.length == 0)
        return "";

      // exclude the last byte for 0-terminated strings
      var n = bytes.length;
      if (type == NpyDataTypes.S && bytes[n - 1] == 0) {
        if (n == 1)
          return "";
        n -= 1;
      }

      return new String(bytes, 0, n, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("failed to read string from NPY data", e);
    }
  }

  public static void write(File file, HeaderDictionary dict, byte[] data) {
    try (var f = new RandomAccessFile(file, "rw");
         var channel = f.getChannel()) {
      var header = dict.toNpyHeader();
      channel.write(ByteBuffer.wrap(header));
      channel.write(ByteBuffer.wrap(data));
    } catch (IOException e) {
      throw new RuntimeException("failed to write NPY data to file " + file, e);
    }
  }

  public static void writeString(File file, String string) {
    //TODO: the data type should be "U" + length here?
    var dict = HeaderDictionary.of(NpyDataTypes.U).create();
    write(file, dict, string.getBytes(StandardCharsets.UTF_8));
  }

  public static void write(File file, NpyArray<?> array) {
    try (var f = new RandomAccessFile(file, "rw");
         var channel = f.getChannel()) {

      var dataType = array.dataType();
      var header = HeaderDictionary.of(dataType)
        .withShape(array.shape())
        .withFortranOrder(array.hasFortranOrder())
        .withByteOrder(ByteOrder.LITTLE_ENDIAN)
        .create()
        .toNpyHeader();
      channel.write(ByteBuffer.wrap(header));

      long totalBytes = (long) dataType.size() * (long) array.size();
      int maxBufferSize = 8 * 1024;
      int bufferSize = totalBytes < maxBufferSize
        ? (int) totalBytes
        : maxBufferSize;

      var buffer = ByteBuffer.allocate(bufferSize)
        .order(ByteOrder.LITTLE_ENDIAN);
      for (int i = 0; i < array.size(); i++) {
        array.writeElementTo(i, buffer);
        if (!buffer.hasRemaining()) {
          buffer.flip();
          channel.write(buffer);
          buffer.clear();
        }
      }
      if (buffer.position() > 0) {
        buffer.flip();
        channel.write(buffer);
      }
    } catch (IOException e) {
      throw new RuntimeException("failed to write array to file " + file, e);
    }
  }

  public static NpyArray<?> memmap(File file)
    throws IOException, NpyFormatException {
    try (var f = new RandomAccessFile(file, "r");
         var channel = f.getChannel()) {
      var header = NpyHeader.read(channel);
      long dataSize = header.dataSize();

      // only a buffer of size < Integer.MAX_VALUE can be mapped
      // into memory. if the size of the stored array is larger
      // we take the normal reader currently
      long max = Integer.MAX_VALUE;
      if (dataSize >= max)
        return ChannelReader.read(channel, header);

      var buffer = channel.map(
        FileChannel.MapMode.READ_ONLY, header.dataOffset(), dataSize);
      buffer.order(header.byteOrder());
      var builder = NpyArrayBuilder.allocate(header);

      long typeSize = header.dataType().size();
      long readBytes = 0;
      while (readBytes < dataSize) {
        builder.next(buffer);
        readBytes += typeSize;
      }
      return builder.build();
    }
  }

  public static NpyArray<?> memmapUnchecked(File file) {
    try {
      return memmap(file);
    } catch (Exception e) {
      throw new RuntimeException("failed to memmap NPY file: " + file, e);
    }
  }

}
