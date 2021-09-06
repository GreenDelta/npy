package org.openlca.npy;

import java.util.function.Function;

import static org.junit.Assert.*;

import org.junit.Test;

public class Array2dTest {

  @Test
  public void testReadRow() {
    Tests.eachNpy(testNpy -> {
      if (testNpy.dataType().size() < 4)
        return;
      var expected = new double[][]{
        {0, 1, 2},
        {3, 4, 5},
      };
      for (int row = 0; row < expected.length; row++) {
        var data = Array2d.readRow(testNpy.file(), row)
          .asDoubleArray()
          .data();
        assertArrayEquals(
          "failed for data type " + testNpy.dataType()
            + " and row " + row, expected[row], data, 1e-16);
      }
    });
  }

  @Test
  public void testReadColumn() {
    Tests.eachNpy(testNpy -> {
      if (testNpy.dataType().size() < 4)
        return;
      var expected = new double[][]{
        {0, 3},
        {1, 4},
        {2, 5},
      };
      for (int col = 0; col < expected.length; col++) {
        var data = Array2d.readColumn(testNpy.file(), col)
          .asDoubleArray()
          .data();
        assertArrayEquals(
          "failed for data type " + testNpy.dataType()
            + " and column " + col, expected[col], data, 1e-16);
      }
    });
  }

  @Test
  public void testReadDiag() {

    Function<NpyDoubleArray, NpyDoubleArray> readDiag = array -> {
      var ref = new Object() {
        NpyArray<?> diag;
      };
      Tests.withFile(file -> {
        Npy.write(file, array);
        ref.diag = Array2d.readDiag(file);
      });
      assertTrue(ref.diag.isDoubleArray());
      var diag = ref.diag.asDoubleArray();
      assertArrayEquals(new int[]{diag.data().length}, diag.shape());
      return diag;
    };

    // square matrix
    var diag = readDiag.apply(NpyDoubleArray.columnOrderOf(
      new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, 3, 3));
    assertArrayEquals(new double[]{1, 5, 9}, diag.data(), 1e-10);

    // more columns than rows
    diag = readDiag.apply(NpyDoubleArray.rowOrderOf(new double[]{
      1, 2, 3,
      4, 5, 6
    }, 2, 3));
    assertArrayEquals(new double[]{1, 5}, diag.data(), 1e-10);

    // more rows than columns
    diag = readDiag.apply(NpyDoubleArray.rowOrderOf(new double[]{
      1, 2, 3,
      4, 5, 6,
      7, 8, 9,
      10, 11, 12
    }, 4, 3));
    assertArrayEquals(new double[]{1, 5, 9}, diag.data(), 1e-10);
  }
}
