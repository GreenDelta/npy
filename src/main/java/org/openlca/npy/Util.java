package org.openlca.npy;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class Util {

  private Util() {
  }

  static short u1ToShort(byte b) {
    return (short) (b & (short) 0xff);
  }

  static short u1ToShort(ByteBuffer buffer) {
    return (short) (buffer.get() & (short) 0xff);
  }

  static int u2ToInt(byte[] bytes, ByteOrder order) {
    var buffer = ByteBuffer.wrap(bytes, 0, 2)
      .order(order);
    return u2ToInt(buffer);
  }

  static int u2ToInt(ByteBuffer buffer) {
    short s = buffer.getShort();
    return s & 0xffff;
  }

  static long u4ToLong(byte[] bytes, ByteOrder order) {
    var buffer = ByteBuffer.wrap(bytes, 0, 4)
      .order(order);
    return u4ToLong(buffer);
  }

  static long u4ToLong(ByteBuffer buffer) {
    int i = buffer.getInt();
    return i & 0xffffffffL;
  }

  static BigInteger u8ToBigInteger(ByteBuffer buffer) {
    long i = buffer.getLong();
    if (i >= 0L)
      return BigInteger.valueOf(i);
    var upper = BigInteger.valueOf(Integer.toUnsignedLong((int) (i >>> 32)));
    var lower = BigInteger.valueOf(Integer.toUnsignedLong((int) i));
    return upper.shiftLeft(32).add(lower);
  }

  static float f2ToFloat(ByteBuffer buffer) {
    return toFloat(buffer.getShort() & 0xffff);
  }

  /**
   * Converts a 16 bit floating point number to a 32 bit floating point number.
   * The 16 bits are stored in the given integer parameter, the higher 16 bits
   * are ignored. This function was directly taken from here:
   * https://stackoverflow.com/a/6162687.
   */
  private static float toFloat(int hbits) {
    int mant = hbits & 0x03ff;           // 10 bits mantissa
    int exp = hbits & 0x7c00;            // 5 bits exponent
    if (exp == 0x7c00)                   // NaN/Inf
      exp = 0x3fc00;                     // -> NaN/Inf
    else if (exp != 0)                   // normalized value
    {
      exp += 0x1c000;                   // exp - 15 + 127
      if (mant == 0 && exp > 0x1c400)   // smooth transition
        return Float.intBitsToFloat((hbits & 0x8000) << 16
                                    | exp << 13 | 0x3ff);
    } else if (mant != 0)                  // && exp==0 -> subnormal
    {
      exp = 0x1c400;                    // make it normal
      do {
        mant <<= 1;                     // mantissa * 2
        exp -= 0x400;                   // decrease exp by 1
      } while ((mant & 0x400) == 0);    // while not normal
      mant &= 0x3ff;                    // discard subnormal bit
    }                                     // else +/-0 -> +/-0
    return Float.intBitsToFloat(          // combine all parts
      (hbits & 0x8000) << 16          // sign  << ( 31 - 15 )
      | (exp | mant) << 13);          // value << ( 23 - 10 )
  }
}
