package org.openlca.npy;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openlca.npy.arrays.Array2D;

public class NpyTest {

  @Test
  public void testLoad() {
    Tests.eachNpy(testNpy -> {
      if (testNpy.dataType() != DataType.f8
        && testNpy.dataType() != DataType.f4
        && testNpy.dataType() != DataType.i4)
        return; // TODO: currently not all types are supported

      System.out.println(testNpy.file());
      var array = Npy.loadUnchecked(testNpy.file());

      switch (testNpy.dataType()) {
        case f8:
          assertTrue(array.isDoubleArray());
          break;
        case f4:
          assertTrue(array.isFloatArray());
          break;
        case i4:
          assertTrue(array.isIntArray());
          break;
      }

      Tests.checkDoubles(array.asDoubleArray());
      Tests.checkFloats(array.asFloatArray());
      Tests.checkInts(array.asIntArray());
    });

  }

}
