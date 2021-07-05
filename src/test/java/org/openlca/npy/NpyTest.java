package org.openlca.npy;

import java.io.File;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import org.junit.Test;

public class NpyTest {

  @Test
  public void testLoad() {
    Tests.eachNpy(testNpy -> {
      var array = Npy.read(testNpy.file());
      Tests.check(testNpy, array);
    });
  }

  @Test
  public void testMemmap() {
    Tests.eachNpy(testNpy -> {
      var array = Npy.memmap(testNpy.file());
      Tests.check(testNpy, array);
    });
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

  @Test
  public void testReadRange() {
    var file = new File(Tests.testDir, "i4_le_c.npy");
    assumeTrue(file.exists());
    var array = Npy.readRange(file, 1, 4);
    assertTrue(array.isIntArray());
    var intArray = array.asIntArray();
    assertArrayEquals(new int[]{1, 2, 3, 4}, intArray.data());
  }

}
