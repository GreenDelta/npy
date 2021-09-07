package org.openlca.npy;

import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class Tests {

  static final File testDir = new File("target/testdata");

  static {
    initTestFiles();
  }

  private Tests() {
  }

  private static void initTestFiles() {

    BooleanSupplier exists = () -> {
      if (!testDir.exists())
        return false;
      var files = testDir.list();
      if (files == null)
        return false;
      for (var file : files) {
        if (file.endsWith(".npy"))
          return true;
      }
      return false;
    };

    if (exists.getAsBoolean())
      return;

    try {
      Files.createDirectories(testDir.toPath());

      var pycmds = new String[]{
        "python3", "python", "py"
      };
      for (var pycmd : pycmds) {
        try {
          Runtime.getRuntime()
            .exec(new String[]{pycmd, "scripts/generate_tests.py"})
            .waitFor();
        } catch (Exception e) {
          System.out.println("Command " + pycmd + " failed");
        }
        if (exists.getAsBoolean()) {
          System.out.println("Created test files");
          break;
        }
      }

    } catch (Exception e) {
      System.err.println("failed to create test files: " + e.getMessage());
    }
  }

  static void withFile(ThrowingConsumer<File> fn) {
    try {
      var tmp = Files.createTempFile("_npy_tests", ".npy");
      fn.accept(tmp.toFile());
      Files.delete(tmp);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void eachNpy(Consumer<TestNpy> fn) {
    for (var type : NpyDataType.values()) {
      if (type.size() == 0)
        continue;
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

  static void check(TestNpy testNpy, NpyArray<?> array) {
    switch (testNpy.dataType()) {
      case bool:
        assertTrue(array.isBooleanArray());
        break;
      case i1:
        assertTrue(array.isByteArray());
        break;
      case i2:
      case u1:
        assertTrue(array.isShortArray());
        break;
      case i4:
      case u2:
        assertTrue(array.isIntArray());
        break;
      case i8:
      case u4:
        assertTrue(array.isLongArray());
        break;
      case u8:
        assertTrue(array.isBigIntegerArray());
        break;
      case f2:
      case f4:
        assertTrue(array.isFloatArray());
        break;
      case f8:
        assertTrue(array.isDoubleArray());
        break;
      default:
        fail("unexpected NPY test type: " + testNpy.dataType());
    }

    checkBooleans(array.asBooleanArray());

    if (!array.isBooleanArray()) {
      checkBytes(array.asByteArray());
      checkDoubles(array.asDoubleArray());
      checkFloats(array.asFloatArray());
      checkInts(array.asIntArray());
      checkLongs(array.asLongArray());
      checkShorts(array.asShortArray());
    }
  }

  private static void checkDoubles(NpyDoubleArray array) {

    assertTrue(Array2d.isValid(array));
    assertEquals(2, Array2d.rowCountOf(array));
    assertEquals(3, Array2d.columnCountOf(array));

    // check each element
    var expected = new double[][]{
      new double[]{0, 1, 2},
      new double[]{3, 4, 5},
    };
    for (int row = 0; row < 2; row++) {
      for (int col = 0; col < 3; col++) {
        assertEquals(expected[row][col], Array2d.get(array, row, col), 1e-3);
      }
    }

    // check by rows
    assertArrayEquals(expected[0], Array2d.getRow(array, 0), 1e-3);
    assertArrayEquals(expected[1], Array2d.getRow(array, 1), 1e-3);

    // check by columns
    assertArrayEquals(new double[]{0, 3}, Array2d.getColumn(array, 0), 1e-3);
    assertArrayEquals(new double[]{1, 4}, Array2d.getColumn(array, 1), 1e-3);
    assertArrayEquals(new double[]{2, 5}, Array2d.getColumn(array, 2), 1e-3);

    // check by storage order
    if (array.hasColumnOrder()) {
      assertArrayEquals(new double[]{0, 3, 1, 4, 2, 5}, array.data(), 1e-3);
    } else {
      assertArrayEquals(new double[]{0, 1, 2, 3, 4, 5}, array.data(), 1e-3);
    }
  }

  private static void checkFloats(NpyFloatArray array) {

    assertTrue(Array2d.isValid(array));
    assertEquals(2, Array2d.rowCountOf(array));
    assertEquals(3, Array2d.columnCountOf(array));

    // check each element
    var expected = new float[][]{
      new float[]{0, 1, 2},
      new float[]{3, 4, 5},
    };
    for (int row = 0; row < 2; row++) {
      for (int col = 0; col < 3; col++) {
        assertEquals(expected[row][col], Array2d.get(array, row, col), 1e-3);
      }
    }

    // check by rows
    assertArrayEquals(expected[0], Array2d.getRow(array, 0), 1e-3f);
    assertArrayEquals(expected[1], Array2d.getRow(array, 1), 1e-3f);

    // check by columns
    assertArrayEquals(new float[]{0, 3}, Array2d.getColumn(array, 0), 1e-3f);
    assertArrayEquals(new float[]{1, 4}, Array2d.getColumn(array, 1), 1e-3f);
    assertArrayEquals(new float[]{2, 5}, Array2d.getColumn(array, 2), 1e-3f);

    // check by storage order
    if (array.hasColumnOrder()) {
      assertArrayEquals(new float[]{0, 3, 1, 4, 2, 5}, array.data(), 1e-3f);
    } else {
      assertArrayEquals(new float[]{0, 1, 2, 3, 4, 5}, array.data(), 1e-3f);
    }
  }

  private static void checkBooleans(NpyBooleanArray array) {

    assertTrue(Array2d.isValid(array));
    assertEquals(2, Array2d.rowCountOf(array));
    assertEquals(3, Array2d.columnCountOf(array));

    // check each element
    var expected = new boolean[][]{
      new boolean[]{false, true, true},
      new boolean[]{true, true, true},
    };
    for (int row = 0; row < 2; row++) {
      for (int col = 0; col < 3; col++) {
        assertEquals(expected[row][col], Array2d.get(array, row, col));
      }
    }

    // check by rows
    assertArrayEquals(expected[0], Array2d.getRow(array, 0));
    assertArrayEquals(expected[1], Array2d.getRow(array, 1));

    // check by columns
    assertArrayEquals(new boolean[]{false, true}, Array2d.getColumn(array, 0));
    assertArrayEquals(new boolean[]{true, true}, Array2d.getColumn(array, 1));
    assertArrayEquals(new boolean[]{true, true}, Array2d.getColumn(array, 2));

    // check by storage order
    assertArrayEquals(
      new boolean[]{false, true, true, true, true, true}, array.data());
  }

  private static void checkBytes(NpyByteArray array) {

    assertTrue(Array2d.isValid(array));
    assertEquals(2, Array2d.rowCountOf(array));
    assertEquals(3, Array2d.columnCountOf(array));

    // check each element
    var expected = new byte[][]{
      new byte[]{0, 1, 2},
      new byte[]{3, 4, 5},
    };
    for (int row = 0; row < 2; row++) {
      for (int col = 0; col < 3; col++) {
        assertEquals(expected[row][col], Array2d.get(array, row, col));
      }
    }

    // check by rows
    assertArrayEquals(expected[0], Array2d.getRow(array, 0));
    assertArrayEquals(expected[1], Array2d.getRow(array, 1));

    // check by columns
    assertArrayEquals(new byte[]{0, 3}, Array2d.getColumn(array, 0));
    assertArrayEquals(new byte[]{1, 4}, Array2d.getColumn(array, 1));
    assertArrayEquals(new byte[]{2, 5}, Array2d.getColumn(array, 2));

    // check by storage order
    if (array.hasColumnOrder()) {
      assertArrayEquals(new byte[]{0, 3, 1, 4, 2, 5}, array.data());
    } else {
      assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5}, array.data());
    }
  }

  private static void checkInts(NpyIntArray array) {

    assertTrue(Array2d.isValid(array));
    assertEquals(2, Array2d.rowCountOf(array));
    assertEquals(3, Array2d.columnCountOf(array));

    // check each element
    var expected = new int[][]{
      new int[]{0, 1, 2},
      new int[]{3, 4, 5},
    };
    for (int row = 0; row < 2; row++) {
      for (int col = 0; col < 3; col++) {
        assertEquals(expected[row][col], Array2d.get(array, row, col));
      }
    }

    // check by rows
    assertArrayEquals(expected[0], Array2d.getRow(array, 0));
    assertArrayEquals(expected[1], Array2d.getRow(array, 1));

    // check by columns
    assertArrayEquals(new int[]{0, 3}, Array2d.getColumn(array, 0));
    assertArrayEquals(new int[]{1, 4}, Array2d.getColumn(array, 1));
    assertArrayEquals(new int[]{2, 5}, Array2d.getColumn(array, 2));

    // check by storage order
    if (array.hasColumnOrder()) {
      assertArrayEquals(new int[]{0, 3, 1, 4, 2, 5}, array.data());
    } else {
      assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5}, array.data());
    }
  }

  private static void checkLongs(NpyLongArray array) {

    assertTrue(Array2d.isValid(array));
    assertEquals(2, Array2d.rowCountOf(array));
    assertEquals(3, Array2d.columnCountOf(array));

    // check each element
    var expected = new long[][]{
      new long[]{0, 1, 2},
      new long[]{3, 4, 5},
    };
    for (int row = 0; row < 2; row++) {
      for (int col = 0; col < 3; col++) {
        assertEquals(expected[row][col], Array2d.get(array, row, col));
      }
    }

    // check by rows
    assertArrayEquals(expected[0], Array2d.getRow(array, 0));
    assertArrayEquals(expected[1], Array2d.getRow(array, 1));

    // check by columns
    assertArrayEquals(new long[]{0, 3}, Array2d.getColumn(array, 0));
    assertArrayEquals(new long[]{1, 4}, Array2d.getColumn(array, 1));
    assertArrayEquals(new long[]{2, 5}, Array2d.getColumn(array, 2));

    // check by storage order
    if (array.hasColumnOrder()) {
      assertArrayEquals(new long[]{0, 3, 1, 4, 2, 5}, array.data());
    } else {
      assertArrayEquals(new long[]{0, 1, 2, 3, 4, 5}, array.data());
    }
  }

  private static void checkShorts(NpyShortArray array) {

    assertTrue(Array2d.isValid(array));
    assertEquals(2, Array2d.rowCountOf(array));
    assertEquals(3, Array2d.columnCountOf(array));

    // check each element
    var expected = new short[][]{
      new short[]{0, 1, 2},
      new short[]{3, 4, 5},
    };
    for (int row = 0; row < 2; row++) {
      for (int col = 0; col < 3; col++) {
        assertEquals(expected[row][col], Array2d.get(array, row, col));
      }
    }

    // check by rows
    assertArrayEquals(expected[0], Array2d.getRow(array, 0));
    assertArrayEquals(expected[1], Array2d.getRow(array, 1));

    // check by columns
    assertArrayEquals(new short[]{0, 3}, Array2d.getColumn(array, 0));
    assertArrayEquals(new short[]{1, 4}, Array2d.getColumn(array, 1));
    assertArrayEquals(new short[]{2, 5}, Array2d.getColumn(array, 2));

    // check by storage order
    if (array.hasColumnOrder()) {
      assertArrayEquals(new short[]{0, 3, 1, 4, 2, 5}, array.data());
    } else {
      assertArrayEquals(new short[]{0, 1, 2, 3, 4, 5}, array.data());
    }
  }

  public static class TestNpy {
    private final File file;
    private final NpyDataType dataType;
    private final ByteOrder byteOrder;
    private final boolean fortranOrder;

    private TestNpy(
      File file,
      NpyDataType dataType,
      ByteOrder byteOrder,
      boolean fortranOrder) {
      this.file = file;
      this.dataType = dataType;
      this.byteOrder = byteOrder;
      this.fortranOrder = fortranOrder;
    }

    private static TestNpy of(
      NpyDataType dataType, String byteOrder, String storageOrder) {
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

    public NpyDataType dataType() {
      return dataType;
    }

    public ByteOrder byteOrder() {
      return byteOrder;
    }

    public boolean hasFortranOrder() {
      return fortranOrder;
    }
  }

  @FunctionalInterface
  public interface ThrowingConsumer<T> {
    void accept(T t) throws Exception;
  }
}
