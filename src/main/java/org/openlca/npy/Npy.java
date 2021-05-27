package org.openlca.npy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class Npy {

  /**
   * Map the content of the given file into NPY array instance. It tries to use
   * memory mapping and avoid data conversions if possible.
   *
   * @param file the NPY file to read
   * @return the mapped NPY array
   * @throws IOException        IO exceptions are rethrown
   * @throws NpyFormatException if the NPY format is invalid or unsupported
   */
  public static NpyArray map(File file) throws IOException, NpyFormatException {
    try (var f = new RandomAccessFile(file, "r");
         var channel = f.getChannel()) {

      var header = Header.read(channel);
      var dict = header.dictionary();
      var offset = header.dataOffset();
      var size = channel.size() - offset;

      var buffer = channel.map(
        FileChannel.MapMode.READ_ONLY, header.dataOffset(), size);
      buffer.order(dict.byteOrder());

      if (dict.dataType() == DataType.f8) {
        double[] data = buffer.asDoubleBuffer().array();
        return new NpyDoubleArray(dict.shape(), data, dict.hasFortranOrder());
      }
      throw new NpyFormatException("unsupported NPY format: " + dict);
    }
  }

  /**
   * Same like {@link #map(File)} but with checked exceptions wrapped into
   * runtime exceptions.
   *
   * @param file the NPY file to read
   * @return the mapped NPY array
   */
  public static NpyArray mapUnchecked(File file) {
    try {
      return map(file);
    } catch (Exception e) {
      throw new RuntimeException("failed to map NPY file: " + file, e);
    }
  }

  public static void load(String path) {
    File file = new File(path);
    if (!file.exists() | !file.isFile()) {
      throw new IllegalArgumentException(
        path + " is not an existing file");
    }
    try (InputStream in = new FileInputStream(file);
         BufferedInputStream buffer = new BufferedInputStream(in)) {
      System.out.println(Header.read(buffer));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
