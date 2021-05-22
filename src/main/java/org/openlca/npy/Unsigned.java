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
    var buffer = ByteBuffer.wrap(bytes, 0, 2)
      .order(order);
    return shortOf(buffer);
  }

  static int shortOf(ByteBuffer buffer) {
    short s = buffer.getShort();
    return s & 0xffff;
  }
  static long intOf(byte[] bytes, ByteOrder order) {
    var buffer = ByteBuffer.wrap(bytes, 0, 4)
      .order(order);
    return intOf(buffer);
  }

  static long intOf(ByteBuffer buffer) {
    int i = buffer.getInt();
    return i & 0xffffffffL;
  }
}
