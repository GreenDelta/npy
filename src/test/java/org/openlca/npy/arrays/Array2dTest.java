package org.openlca.npy.arrays;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openlca.npy.Tests;

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

}
