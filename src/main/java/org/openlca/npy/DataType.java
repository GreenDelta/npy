package org.openlca.npy;

/**
 * An enumeration of the supported NumPy data types.
 * https://numpy.org/doc/stable/reference/arrays.dtypes.html
 * https://numpy.org/doc/stable/reference/arrays.interface.html#arrays-interface
 */
public enum DataType {

  /**
   * 8-bit signed integers
   */
  i("b"),

  /**
   * 8-bit unsigned integers
   */
  u("B"),

  /**
   * 16 bit signed integers
   */
  i2("i2"),

  /**
   * 16 bit unsigned integers
   */
  u2("u2"),

  /**
   * 32 bit signed integers
   */
  i4("i4"),

  /**
   * 32 bit floating point numbers
   */
  f4("f4"),

  /**
   * 32 bit unsigned integers
   */
  u4("u4"),

  /**
   * 64 bit signed integers
   */
  i8("i8"),

  /**
   * 64 bit floating point numbers
   */
  f8("f8"),

  /**
   * 64 bit unsigned integers
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

  public String symbol() {
    return symbol;
  }
}
