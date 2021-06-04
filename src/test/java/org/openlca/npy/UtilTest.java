package org.openlca.npy;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilTest {

  @Test
  public void testU1ToByte() {
    assertEquals(0, Util.u1ToShort((byte) 0x00));
    assertEquals(1, Util.u1ToShort((byte) 0x01));
    assertEquals(255, Util.u1ToShort((byte) 0xff));
    assertEquals(0x93, Util.u1ToShort((byte) 0x93));
  }

  @Test
  public void testU2ToInt() {
    assertEquals(65535, Util.u2ToInt(
      new byte[]{(byte) 0xff, (byte) 0xff},
      ByteOrder.LITTLE_ENDIAN));
  }

  @Test
  public void testU8ToBigInteger() {
    var bytes = new byte[] {
      (byte) 0xff, (byte) 0xff,
      (byte) 0xff, (byte) 0xff,
      (byte) 0xff, (byte) 0xff,
      (byte) 0xff, (byte) 0xff,
    };
    var buffer = ByteBuffer.wrap(bytes);
    var u8Max = Util.u8ToBigInteger(buffer);
    assertEquals("18446744073709551615", u8Max.toString());
  }
}
