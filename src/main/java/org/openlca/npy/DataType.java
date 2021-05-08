package org.openlca.npy;

/**
 * An enumeration of the supported NumPy data types.
 * https://numpy.org/doc/stable/reference/arrays.dtypes.html
 * https://numpy.org/doc/stable/reference/arrays.interface.html#arrays-interface
 */
public enum DataType {

  /**
   * A signed byte, 8-bit integer
   */
  i("b"),

  /**
   * A unsigned byte, 8-bit integer.
   */
  u("B"),

  /**
   * A 2-byte, 16 bit signed integer.
   */
  i2("i2"),

  /**
   * A 2-byte, 16 bit unsigned integer.
   */
  u2("u2"),

  /**
   * A 4-byte, 32 bit signed integer.
   */
  i4("i4"),

  /**
   * A 4-byte, 32 bit unsigned integer.
   */
  u4("u4"),

  /**
   * A 8-byte, 64 bit signed integer.
   */
  i8("i8"),

  /**
   * A 8-byte, 64 bit unsigned integer.
   */
  u8("u4");



  private final String symbol;

  DataType(String symbol) {
    this.symbol = symbol;
  }

  @Override
  public String toString() {
    return symbol;
  }
}
