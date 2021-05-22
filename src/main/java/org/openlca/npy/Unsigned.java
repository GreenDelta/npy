package org.openlca.npy;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class Unsigned {

  private Unsigned() {
  }

  static int byteOf(byte b) {
    return b & 0xff;
  }

  static int shortOf(byte[] bytes, ByteOrder order) {
    short s = ByteBuffer.wrap(bytes, 0, 2)
      .order(order)
      .getShort();
    return s & 0xffff;
  }

  static long intOf(byte[] bytes, ByteOrder order) {
    int i = ByteBuffer.wrap(bytes, 0, 4)
      .order(order)
      .getInt();
    return i & 0xffffffffL;
  }
}
