package org.openlca.npy;

import java.util.EnumSet;

import static org.junit.Assert.*;

import org.junit.Test;

public class NpyTest {

  @Test
  public void testLoad() {
    Tests.eachNpy(testNpy -> {
      if (!EnumSet.of(
        DataType.bool,
        DataType.f2,
        DataType.f4,
        DataType.f8,
        DataType.i1,
        DataType.i2,
        DataType.i4,
        DataType.i8,
        DataType.u1,
        DataType.u2,
        DataType.u4,
        DataType.u8).contains(testNpy.dataType()))
        return; // TODO: currently not all types are supported

      System.out.println(testNpy.file());
      var array = Npy.loadUnchecked(testNpy.file());

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
      }

      Tests.checkBooleans(array.asBooleanArray());

      if (!array.isBooleanArray()) {
        Tests.checkBytes(array.asByteArray());
        Tests.checkDoubles(array.asDoubleArray());
        Tests.checkFloats(array.asFloatArray());
        Tests.checkInts(array.asIntArray());
        Tests.checkLongs(array.asLongArray());
        Tests.checkShorts(array.asShortArray());
      }
    });
  }

}
