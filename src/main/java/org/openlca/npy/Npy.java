package org.openlca.npy;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.function.BiConsumer;

import org.openlca.npy.arrays.NpyArray;
import org.openlca.npy.dict.NpyHeaderDict;

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

  public static NpyArray<?> read(ReadableByteChannel channel) {
    try {
      var header = NpyHeader.read(channel);
      return ChannelReader.read(channel, header);
    } catch (IOException e) {
      throw new RuntimeException("failed to read NPY array from channel", e);
    }
  }

  /**
   * Reads a range of {@code n} array elements from an NPY file.
   *
   * @param file   a NPY file
   * @param offset the position of the first of the range
   * @param n      the number of elements that should be read from the file
   * @return an one-dimensional array with {@code n} elements
   */
  public static NpyArray<?> readRange(File file, int offset, int n) {
    try (var raf = new RandomAccessFile(file, "r");
         var channel = raf.getChannel()) {
      var header = NpyHeader.read(channel);
      return readRange(raf, header, offset, n);
    } catch (IOException e) {
      throw new RuntimeException("failed to read a range of " + n +
        " elements from NPY file " + file, e);
    }
  }

  public static void use(File file, BiConsumer<RandomAccessFile, NpyHeader> fn) {
    try (var raf = new RandomAccessFile(file, "r");
         var channel = raf.getChannel()) {
      var header = NpyHeader.read(channel);
      fn.accept(raf, header);
    } catch (IOException e) {
      throw new RuntimeException("failed to use NPY file: " + file, e);
    }
  }

  public static NpyArray<?> readRange(
    RandomAccessFile file, NpyHeader header, int offset, int n) {

    var dict = header.dict();
    int elemSize = dict.dataType() == NpyDataType.U
      ? 4
      : Math.max(dict.dataType().size(), 1);

    try {

      // seek to the reading position and read the data
      long start = header.dataOffset();
      if (offset > 0) {
        start += (long) elemSize * (long) offset;
      }
      file.seek(start);
      var buffer = ByteBuffer.allocate(n * elemSize);
      file.getChannel().read(buffer);

      // build the range array
      var rangeDict = NpyHeaderDict.of(dict.dataType())
        .withTypeSize(dict.typeSize())
        .withByteOrder(dict.byteOrder())
        .withFortranOrder(dict.hasFortranOrder())
        .withShape(new int[]{n})
        .create();
      var reader = NpyArrayReader.allocate(rangeDict);
      reader.readAllFrom(buffer);
      return reader.finish();

    } catch (IOException e) {
      throw new RuntimeException(
        "failed to read range from NPY file: " + file, e);
    }
  }

  public static void write(File file, NpyHeaderDict dict, byte[] data) {
    try (var f = new RandomAccessFile(file, "rw");
         var channel = f.getChannel()) {
      var header = dict.toNpyHeader();
      channel.write(ByteBuffer.wrap(header));
      channel.write(ByteBuffer.wrap(data));
    } catch (IOException e) {
      throw new RuntimeException("failed to write NPY data to file " + file, e);
    }
  }

  public static void write(File file, NpyArray<?> array) {
    try (var f = new RandomAccessFile(file, "rw");
         var channel = f.getChannel()) {
      write(channel, array);
    } catch (IOException e) {
      throw new RuntimeException("failed to write array to file " + file, e);
    }
  }

  public static void write(WritableByteChannel channel, NpyArray<?> array) {
    try {

      // write the header
      var dataType = array.dataType();
      var dict = NpyHeaderDict.of(dataType)
        .withShape(array.shape())
        .withFortranOrder(array.hasFortranOrder())
        .withByteOrder(NpyByteOrder.LITTLE_ENDIAN)
        .create();
      channel.write(ByteBuffer.wrap(dict.toNpyHeader()));

      // allocate a buffer
      long totalBytes = dict.dataSize();
      int maxBufferSize = 8 * 1024;
      int bufferSize = totalBytes < maxBufferSize
        ? (int) totalBytes
        : maxBufferSize;
      var buffer = ByteBuffer.allocate(bufferSize)
        .order(ByteOrder.LITTLE_ENDIAN);

      // write data to the channel
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
      throw new RuntimeException("failed to write NPY array to channel", e);
    }
  }

  public static void write(OutputStream stream, NpyArray<?> array) {
    // do not close the channel here because it would
    // close the underlying output stream which is
    // not the idea of this function.
    var channel = Channels.newChannel(stream);
    write(channel, array);
  }

  public static NpyArray<?> memmap(File file) {
    try (var f = new RandomAccessFile(file, "r");
         var channel = f.getChannel()) {
      var header = NpyHeader.read(channel);
      long dataSize = header.dict().dataSize();

      // only a buffer of size < Integer.MAX_VALUE can be mapped
      // into memory. if the size of the stored array is larger
      // we take the normal reader currently
      long max = Integer.MAX_VALUE;
      if (dataSize >= max)
        return ChannelReader.read(channel, header);

      var buffer = channel.map(
        FileChannel.MapMode.READ_ONLY, header.dataOffset(), dataSize);
      buffer.order(header.byteOrder());
      var builder = NpyArrayReader.allocate(header.dict());
      builder.readAllFrom(buffer);
      return builder.finish();
    } catch (IOException e) {
      throw new RuntimeException("failed to memmap NPY file: " + file, e);
    }
  }

}
