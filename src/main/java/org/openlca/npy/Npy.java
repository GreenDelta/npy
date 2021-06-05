package org.openlca.npy;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import org.openlca.npy.arrays.NpyArray;

public class Npy {

  /**
   * Load the content of the given file into NPY array instance.
   *
   * @param file the NPY file to read
   * @return the mapped NPY array
   * @throws IOException        IO exceptions are rethrown
   * @throws NpyFormatException if the NPY format is invalid or unsupported
   */
  public static NpyArray<?> load(File file)
    throws IOException, NpyFormatException {
    try (var f = new RandomAccessFile(file, "r");
         var channel = f.getChannel()) {
      var header = NpyHeader.read(channel);
      return ChannelReader.read(channel, header);
    }
  }

  /**
   * Same like {@link #load(File)} but with checked exceptions wrapped into
   * runtime exceptions.
   *
   * @param file the NPY file to read
   * @return the mapped NPY array
   */
  public static NpyArray<?> loadUnchecked(File file) {
    try {
      return load(file);
    } catch (Exception e) {
      throw new RuntimeException("failed to load NPY file: " + file, e);
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
