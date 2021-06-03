package org.openlca.npy;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class Unsigned {

  private Unsigned() {
  }

  static short u1ToSigned(byte b) {
    return (short) (b & (short) 0xff);
  }

  static int u2ToSigned(byte[] bytes, ByteOrder order) {
    var buffer = ByteBuffer.wrap(bytes, 0, 2)
      .order(order);
    return u2ToSigned(buffer);
  }

  static int u2ToSigned(ByteBuffer buffer) {
    short s = buffer.getShort();
    return s & 0xffff;
  }

  static long u4ToSigned(byte[] bytes, ByteOrder order) {
    var buffer = ByteBuffer.wrap(bytes, 0, 4)
      .order(order);
    return u4ToSigned(buffer);
  }

  static long u4ToSigned(ByteBuffer buffer) {
    int i = buffer.getInt();
    return i & 0xffffffffL;
  }
}
