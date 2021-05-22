package org.openlca.npy;

import java.nio.ByteBuffer;

class Unsigned {

  private Unsigned() {
  }

  static int byteOf(byte b) {
    return b & 0xff;
  }

  static int shortOf(byte[] bytes) {
    short s = ByteBuffer.wrap(bytes, 0, 2).getShort();
    return s & 0xffff;
  }

  static long intOf(byte[] bytes) {
    int i = ByteBuffer.wrap(bytes, 0, 4).getInt();
    return i & 0xffffffffL;
  }
}
