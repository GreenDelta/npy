package org.openlca.npy;

import java.io.File;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import org.junit.Test;
import org.openlca.npy.arrays.NpyArray;

public class NpyTest {

  @Test
  public void testLoad() {
    Tests.eachNpy(testNpy -> {
      var array = Npy.read(testNpy.file());
      runChecks(testNpy, array);
    });
  }

  @Test
  public void testMemmap() {
    Tests.eachNpy(testNpy -> {
      var array = Npy.memmap(testNpy.file());
      runChecks(testNpy, array);
    });
  }

  private void runChecks(Tests.TestNpy testNpy, NpyArray<?> array) {
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
  }

  @Test
  public void testReadAscii() {
    var file = new File(Tests.testDir, "S.npy");
    assumeTrue(file.exists());
    var array = Npy.read(file);
    assertTrue(array.isCharArray());
    assertEquals(NpyDataType.S, array.dataType());
    assertEquals("uebergross", array.toString());
  }

  @Test
  public void testReadUnicode() {
    var file = new File(Tests.testDir, "U.npy");
    assumeTrue(file.exists());
    var array = Npy.read(file);
    assertTrue(array.isCharArray());
    assertEquals(NpyDataType.U, array.dataType());
    assertEquals("übergroß", array.toString());
  }

}
