package org.openlca.npy;

import static org.junit.Assert.*;

import org.junit.Test;

public class NpyTest {

  @Test
  public void testMap() {
    Tests.eachNpy(testNpy -> {
      if (testNpy.dataType() != DataType.f8)
        return; // TODO: currently only f8 is supported

      System.out.println(testNpy.file());
      var array = Npy.loadUnchecked(testNpy.file());
      assertTrue(array.isDoubleArray());
      assertTrue(Array2D.isValid(array));
      assertEquals(2, Array2D.rowCountOf(array));
      assertEquals(3, Array2D.columnCountOf(array));
      checkDoubles(array.asDoubleArray());
    });

  }

  private void checkDoubles(NpyDoubleArray array) {

    // check each element
    var expected = new double[][]{
      new double[]{0, 1, 2},
      new double[]{3, 4, 5},
    };
    for (int row = 0; row < 2; row++) {
      for (int col = 0; col < 3; col++) {
        assertEquals(expected[row][col], Array2D.get(array, row, col), 1e-16);
      }
    }

    // check by rows
    assertArrayEquals(expected[0], Array2D.getRow(array, 0), 1e-16);
    assertArrayEquals(expected[1], Array2D.getRow(array, 1), 1e-16);

    // check by columns
    assertArrayEquals(new double[]{0, 3}, Array2D.getColumn(array, 0), 1e-16);
    assertArrayEquals(new double[]{1, 4}, Array2D.getColumn(array, 1), 1e-16);
    assertArrayEquals(new double[]{2, 5}, Array2D.getColumn(array, 2), 1e-16);

    // check by storage order
    if (array.hasFortranOrder()) {
      assertArrayEquals(new double[]{0, 3, 1, 4, 2, 5}, array.data, 1e-16);
    } else {
      assertArrayEquals(new double[]{0, 1, 2, 3, 4, 5}, array.data, 1e-16);
    }
  }
}
