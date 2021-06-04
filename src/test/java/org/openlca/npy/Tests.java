package org.openlca.npy;

import java.io.File;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import org.openlca.npy.arrays.Array2D;
import org.openlca.npy.arrays.NpyBooleanArray;
import org.openlca.npy.arrays.NpyByteArray;
import org.openlca.npy.arrays.NpyDoubleArray;
import org.openlca.npy.arrays.NpyFloatArray;
import org.openlca.npy.arrays.NpyIntArray;
import org.openlca.npy.arrays.NpyLongArray;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

  public static Optional<TestNpy> getNpy(
    DataType type, ByteOrder byteOrder, boolean fortranOrder) {
    var npy = new Object() {
      TestNpy npy;
    };
    eachNpy(testNpy -> {
      if (Objects.equals(type, testNpy.dataType)
          && Objects.equals(byteOrder, testNpy.byteOrder)
          && fortranOrder == testNpy.fortranOrder) {
        npy.npy = testNpy;
      }
    });
    return Optional.ofNullable(npy.npy);
  }

  static void checkDoubles(NpyDoubleArray array) {

    assertTrue(Array2D.isValid(array));
    assertEquals(2, Array2D.rowCountOf(array));
    assertEquals(3, Array2D.columnCountOf(array));

    // check each element
    var expected = new double[][]{
      new double[]{0, 1, 2},
      new double[]{3, 4, 5},
    };
    for (int row = 0; row < 2; row++) {
      for (int col = 0; col < 3; col++) {
        assertEquals(expected[row][col], Array2D.get(array, row, col), 1e-3);
      }
    }

    // check by rows
    assertArrayEquals(expected[0], Array2D.getRow(array, 0), 1e-3);
    assertArrayEquals(expected[1], Array2D.getRow(array, 1), 1e-3);

    // check by columns
    assertArrayEquals(new double[]{0, 3}, Array2D.getColumn(array, 0), 1e-3);
    assertArrayEquals(new double[]{1, 4}, Array2D.getColumn(array, 1), 1e-3);
    assertArrayEquals(new double[]{2, 5}, Array2D.getColumn(array, 2), 1e-3);

    // check by storage order
    if (array.hasFortranOrder()) {
      assertArrayEquals(new double[]{0, 3, 1, 4, 2, 5}, array.data(), 1e-3);
    } else {
      assertArrayEquals(new double[]{0, 1, 2, 3, 4, 5}, array.data(), 1e-3);
    }
  }

  static void checkFloats(NpyFloatArray array) {

    assertTrue(Array2D.isValid(array));
    assertEquals(2, Array2D.rowCountOf(array));
    assertEquals(3, Array2D.columnCountOf(array));

    // check each element
    var expected = new float[][]{
      new float[]{0, 1, 2},
      new float[]{3, 4, 5},
    };
    for (int row = 0; row < 2; row++) {
      for (int col = 0; col < 3; col++) {
        assertEquals(expected[row][col], Array2D.get(array, row, col), 1e-3);
      }
    }

    // check by rows
    assertArrayEquals(expected[0], Array2D.getRow(array, 0), 1e-3f);
    assertArrayEquals(expected[1], Array2D.getRow(array, 1), 1e-3f);

    // check by columns
    assertArrayEquals(new float[]{0, 3}, Array2D.getColumn(array, 0), 1e-3f);
    assertArrayEquals(new float[]{1, 4}, Array2D.getColumn(array, 1), 1e-3f);
    assertArrayEquals(new float[]{2, 5}, Array2D.getColumn(array, 2), 1e-3f);

    // check by storage order
    if (array.hasFortranOrder()) {
      assertArrayEquals(new float[]{0, 3, 1, 4, 2, 5}, array.data(), 1e-3f);
    } else {
      assertArrayEquals(new float[]{0, 1, 2, 3, 4, 5}, array.data(), 1e-3f);
    }
  }

  static void checkBooleans(NpyBooleanArray array) {

    assertTrue(Array2D.isValid(array));
    assertEquals(2, Array2D.rowCountOf(array));
    assertEquals(3, Array2D.columnCountOf(array));

    // check each element
    var expected = new boolean[][]{
      new boolean[]{false, true, true},
      new boolean[]{true, true, true},
    };
    for (int row = 0; row < 2; row++) {
      for (int col = 0; col < 3; col++) {
        assertEquals(expected[row][col], Array2D.get(array, row, col));
      }
    }

    // check by rows
    assertArrayEquals(expected[0], Array2D.getRow(array, 0));
    assertArrayEquals(expected[1], Array2D.getRow(array, 1));

    // check by columns
    assertArrayEquals(new boolean[]{false, true}, Array2D.getColumn(array, 0));
    assertArrayEquals(new boolean[]{true, true}, Array2D.getColumn(array, 1));
    assertArrayEquals(new boolean[]{true, true}, Array2D.getColumn(array, 2));

    // check by storage order
    if (array.hasFortranOrder()) {
      assertArrayEquals(new boolean[]{false, true, true, true, true, true}, array.data());
    } else {
      assertArrayEquals(new boolean[]{false, true, true, true, true, true}, array.data());
    }
  }

  static void checkBytes(NpyByteArray array) {

    assertTrue(Array2D.isValid(array));
    assertEquals(2, Array2D.rowCountOf(array));
    assertEquals(3, Array2D.columnCountOf(array));

    // check each element
    var expected = new byte[][]{
      new byte[]{0, 1, 2},
      new byte[]{3, 4, 5},
    };
    for (int row = 0; row < 2; row++) {
      for (int col = 0; col < 3; col++) {
        assertEquals(expected[row][col], Array2D.get(array, row, col));
      }
    }

    // check by rows
    assertArrayEquals(expected[0], Array2D.getRow(array, 0));
    assertArrayEquals(expected[1], Array2D.getRow(array, 1));

    // check by columns
    assertArrayEquals(new byte[]{0, 3}, Array2D.getColumn(array, 0));
    assertArrayEquals(new byte[]{1, 4}, Array2D.getColumn(array, 1));
    assertArrayEquals(new byte[]{2, 5}, Array2D.getColumn(array, 2));

    // check by storage order
    if (array.hasFortranOrder()) {
      assertArrayEquals(new byte[]{0, 3, 1, 4, 2, 5}, array.data());
    } else {
      assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5}, array.data());
    }
  }

  static void checkInts(NpyIntArray array) {

    assertTrue(Array2D.isValid(array));
    assertEquals(2, Array2D.rowCountOf(array));
    assertEquals(3, Array2D.columnCountOf(array));

    // check each element
    var expected = new int[][]{
      new int[]{0, 1, 2},
      new int[]{3, 4, 5},
    };
    for (int row = 0; row < 2; row++) {
      for (int col = 0; col < 3; col++) {
        assertEquals(expected[row][col], Array2D.get(array, row, col));
      }
    }

    // check by rows
    assertArrayEquals(expected[0], Array2D.getRow(array, 0));
    assertArrayEquals(expected[1], Array2D.getRow(array, 1));

    // check by columns
    assertArrayEquals(new int[]{0, 3}, Array2D.getColumn(array, 0));
    assertArrayEquals(new int[]{1, 4}, Array2D.getColumn(array, 1));
    assertArrayEquals(new int[]{2, 5}, Array2D.getColumn(array, 2));

    // check by storage order
    if (array.hasFortranOrder()) {
      assertArrayEquals(new int[]{0, 3, 1, 4, 2, 5}, array.data());
    } else {
      assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5}, array.data());
    }
  }

  static void checkLongs(NpyLongArray array) {

    assertTrue(Array2D.isValid(array));
    assertEquals(2, Array2D.rowCountOf(array));
    assertEquals(3, Array2D.columnCountOf(array));

    // check each element
    var expected = new long[][]{
      new long[]{0, 1, 2},
      new long[]{3, 4, 5},
    };
    for (int row = 0; row < 2; row++) {
      for (int col = 0; col < 3; col++) {
        assertEquals(expected[row][col], Array2D.get(array, row, col));
      }
    }

    // check by rows
    assertArrayEquals(expected[0], Array2D.getRow(array, 0));
    assertArrayEquals(expected[1], Array2D.getRow(array, 1));

    // check by columns
    assertArrayEquals(new long[]{0, 3}, Array2D.getColumn(array, 0));
    assertArrayEquals(new long[]{1, 4}, Array2D.getColumn(array, 1));
    assertArrayEquals(new long[]{2, 5}, Array2D.getColumn(array, 2));

    // check by storage order
    if (array.hasFortranOrder()) {
      assertArrayEquals(new long[]{0, 3, 1, 4, 2, 5}, array.data());
    } else {
      assertArrayEquals(new long[]{0, 1, 2, 3, 4, 5}, array.data());
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
