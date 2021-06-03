package org.openlca.npy;

import java.util.EnumSet;

import static org.junit.Assert.*;

import org.junit.Test;

public class NpyTest {

  @Test
  public void testLoad() {
    Tests.eachNpy(testNpy -> {
      if (!EnumSet.of(
        DataType.f4,
        DataType.f8,
        DataType.i4,
        DataType.i8,
        DataType.u4).contains(testNpy.dataType()))
        return; // TODO: currently not all types are supported

      System.out.println(testNpy.file());
      var array = Npy.loadUnchecked(testNpy.file());

      switch (testNpy.dataType()) {
        case i4:
          assertTrue(array.isIntArray());
          break;
        case i8:
          assertTrue(array.isLongArray());
          break;
        case f4:
          assertTrue(array.isFloatArray());
          break;
        case f8:
          assertTrue(array.isDoubleArray());
          break;
        case u4:
          assertTrue(array.isLongArray());
          break;
      }

      Tests.checkDoubles(array.asDoubleArray());
      Tests.checkFloats(array.asFloatArray());
      Tests.checkInts(array.asIntArray());
      Tests.checkLongs(array.asLongArray());
    });

  }

}
