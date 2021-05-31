package org.openlca.npy;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openlca.npy.arrays.Array2D;

public class NpyTest {

  @Test
  public void testLoad() {
    Tests.eachNpy(testNpy -> {
      if (testNpy.dataType() != DataType.f8)
        return; // TODO: currently only f8 is supported

      System.out.println(testNpy.file());
      var array = Npy.loadUnchecked(testNpy.file());
      assertTrue(array.isDoubleArray());
      Tests.checkDoubles(array.asDoubleArray());
      Tests.checkFloats(array.asFloatArray());
    });

  }

}
