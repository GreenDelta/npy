package org.openlca.npy;

import static org.junit.Assert.*;

import java.nio.ByteOrder;

import org.junit.Test;

public class DataTypeTest {

  @Test
  public void testByteOrder() {
    var dtypes = new String[] {
      null,
      "",
      "i4",
      "=i4",
      "<i4",
      ">i4",
      "|?"
    };
    var expected = new ByteOrder[] {
      ByteOrder.nativeOrder(),
      ByteOrder.nativeOrder(),
      ByteOrder.nativeOrder(),
      ByteOrder.nativeOrder(),
      ByteOrder.LITTLE_ENDIAN,
      ByteOrder.BIG_ENDIAN,
      ByteOrder.nativeOrder(),
    };
    for (int i = 0; i < dtypes.length; i++) {
      assertEquals(expected[i], DataType.byteOrderOf(dtypes[i]));
    }
  }

}
