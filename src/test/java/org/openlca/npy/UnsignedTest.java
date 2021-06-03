package org.openlca.npy;

import static org.junit.Assert.*;

import org.junit.Test;

public class UnsignedTest {

  @Test
  public void testByteOf() {
    assertEquals(0, Unsigned.u1ToSigned((byte) 0x00));
    assertEquals(1, Unsigned.u1ToSigned((byte) 0x01));
    assertEquals(255, Unsigned.u1ToSigned((byte) 0xff));
    assertEquals(0x93, Unsigned.u1ToSigned((byte) 0x93));
  }

  @Test
  public void testShortOf() {

  }
}
