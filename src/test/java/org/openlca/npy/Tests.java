package org.openlca.npy;

import java.io.File;
import java.nio.ByteOrder;
import java.util.List;
import java.util.function.Consumer;

import org.openlca.npy.arrays.Array2D;
import org.openlca.npy.arrays.NpyArray;
import org.openlca.npy.arrays.NpyBooleanArray;
import org.openlca.npy.arrays.NpyByteArray;
import org.openlca.npy.arrays.NpyDoubleArray;
import org.openlca.npy.arrays.NpyFloatArray;
import org.openlca.npy.arrays.NpyIntArray;
import org.openlca.npy.arrays.NpyLongArray;
import org.openlca.npy.arrays.NpyShortArray;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class Tests {

  static final File testDir = new File("target/testdata");

  private Tests() {
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

  private static void checkFloats(NpyFloatArray array) {

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

  private static void checkBooleans(NpyBooleanArray array) {

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
    assertArrayEquals(
      new boolean[]{false, true, true, true, true, true}, array.data());
  }

  private static void checkBytes(NpyByteArray array) {

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

  private static void checkInts(NpyIntArray array) {

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

  private static void checkLongs(NpyLongArray array) {

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

  private static void checkShorts(NpyShortArray array) {

    assertTrue(Array2D.isValid(array));
    assertEquals(2, Array2D.rowCountOf(array));
    assertEquals(3, Array2D.columnCountOf(array));

    // check each element
    var expected = new short[][]{
      new short[]{0, 1, 2},
      new short[]{3, 4, 5},
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
    assertArrayEquals(new short[]{0, 3}, Array2D.getColumn(array, 0));
    assertArrayEquals(new short[]{1, 4}, Array2D.getColumn(array, 1));
    assertArrayEquals(new short[]{2, 5}, Array2D.getColumn(array, 2));

    // check by storage order
    if (array.hasFortranOrder()) {
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
}
