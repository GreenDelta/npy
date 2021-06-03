package org.openlca.npy;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

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

      /*
      var offset = header.dataOffset();
      var size = channel.size() - offset;

      var buffer = channel.map(
        FileChannel.MapMode.READ_ONLY, header.dataOffset(), size);
      buffer.order(header.byteOrder());

      if (header.dataType() == DataType.f8) {
        double[] data = new double[header.numberOfElements()];
        buffer.asDoubleBuffer().get(data);
        return new NpyDoubleArray(header.shape(), data, header.hasFortranOrder());
      }
      throw new NpyFormatException("unsupported NPY format: " + header);

       */
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
      throw new RuntimeException("failed to map NPY file: " + file, e);
    }
  }
}
