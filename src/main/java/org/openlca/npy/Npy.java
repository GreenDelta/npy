package org.openlca.npy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Npy {

  public static NpyArray map(File file) {
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
