package org.openlca.npy;

import java.io.File;
import java.nio.ByteOrder;
import java.util.List;
import java.util.function.Consumer;

public class Tests {

  private static final File testDir = new File("target/testdata");

  private Tests() {
  }

  public static void eachNpy(Consumer<TestNpy> fn) {
    for (var type : DataType.values()) {
      for (var byteOrder : List.of("be", "le")) {
        for (var storageOrder : List.of("c", "f")) {
          var npy = TestNpy.of(type, byteOrder, storageOrder);
          if (npy != null) {
            fn.accept(npy);
          }
        }
      }
    }
  }

  public static class TestNpy {
    private final File file;
    private final DataType dataType;
    private final ByteOrder byteOrder;
    private final boolean fortranOrder;

    private TestNpy(
      File file,
      DataType dataType,
      ByteOrder byteOrder,
      boolean fortranOrder) {
      this.file = file;
      this.dataType = dataType;
      this.byteOrder = byteOrder;
      this.fortranOrder = fortranOrder;
    }

    private static TestNpy of(
      DataType dataType, String byteOrder, String storageOrder) {
      var fileName = dataType.name() + "_"
                     + byteOrder + "_"
                     + storageOrder + ".npy";
      var file = new File(testDir, fileName);
      if (!file.exists()) {
        System.err.println("test file is missing: " + file.getAbsolutePath());
        return null;
      }
      return new TestNpy(
        file,
        dataType,
        byteOrder.equals("be")
          ? ByteOrder.BIG_ENDIAN
          : ByteOrder.LITTLE_ENDIAN,
        storageOrder.equals("f")
      );
    }

    public File file() {
      return file;
    }

    public DataType dataType() {
      return dataType;
    }

    public ByteOrder byteOrder() {
      return byteOrder;
    }

    public boolean hasFortranOrder() {
      return fortranOrder;
    }
  }
}
