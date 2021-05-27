package org.openlca.npy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class Npy {

  /**
   * Map the content of the given file into NPY array instance. It tries to use
   * memory mapping and avoid data conversions if possible.
   *
   * @param file the NPY file to read
   * @return
   */
  public static NpyArray map(File file) throws IOException {
    try (var f = new RandomAccessFile(file, "r")) {

    }
    return null;
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
