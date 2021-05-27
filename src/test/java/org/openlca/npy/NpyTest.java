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
      var array = Npy.mapUnchecked(testNpy.file());
      assertTrue(array.isDoubleArray());
      assertTrue(Array2D.isValid(array));
      assertEquals(2, Array2D.rowCountOf(array));
      assertEquals(3, Array2D.columnCountOf(array));

      var doubles = array.asDoubleArray();
      assertArrayEquals(
        new double[]{0, 1, 2}, Array2D.getRow(doubles, 0), 1e-9);
      assertArrayEquals(
        new double[]{3, 4, 5}, Array2D.getRow(doubles, 1), 1e-9);
    });

  }
}
