package org.openlca.npy;

import java.nio.ByteOrder;

/**
 * An enumeration of the supported NumPy data types.
 * https://numpy.org/doc/stable/reference/arrays.dtypes.html
 * https://numpy.org/doc/stable/reference/arrays.interface.html#arrays-interface
 */
public enum DataType {

  bool("?", new String[]{}),

  /**
   * 8-bit signed integers
   */
  i1("b", new String[]{}),

  /**
   * 8-bit unsigned integers
   */
  u1("B", new String[]{}),

  /**
   * 16 bit signed integers
   */
  i2("i2", new String[]{}),

  /**
   * 16 bit unsigned integers
   */
  u2("u2", new String[]{}),

  /**
   * 32 bit signed integers
   */
  i4("i4", new String[]{}),

  /**
   * 32 bit floating point numbers
   */
  f4("f4", new String[]{}),

  /**
   * 32 bit unsigned integers
   */
  u4("u4", new String[]{}),

  /**
   * 64 bit signed integers
   */
  i8("i8", new String[]{}),

  /**
   * 64 bit floating point numbers
   */
  f8("f8", new String[]{}),

  /**
   * 64 bit unsigned integers
   */
  u8("u4", new String[]{});


  private final String symbol;

  private final int size;
  private final String[] synonyms;

  DataType(String symbol, String[] synonyms) {
    this.symbol = symbol;
    this.synonyms = synonyms;
  }

  @Override
  public String toString() {
    return symbol;
  }

  /**
   * Get the primary symbol of the data type.
   */
  public String symbol() {
    return symbol;
  }

  /**
   * Tries to find the data type for the given symbol. The behavior is a bit
   * like {@code numpy.dtype('[the type name or symbol]')}. The given symbol
   * can have a prefix that indicates the byte order.
   *
   * @param dtype the data type symbol (e.g. {@code i4, int32, <i4})
   * @return the data type or {@code null} if there is no matching type defined.
   */
  public static DataType of(String dtype) {
    if (dtype == null || dtype.length() == 0)
      return null;
    char first = dtype.charAt(0);
    boolean hasOrderMark = first == '<'
                           || first == '>'
                           || first == '='
                           || first == '|';
    var symbol = hasOrderMark
      ? dtype.substring(1)
      : dtype;
    for (var type : DataType.values()) {
      if (symbol.equals(type.symbol()))
        return type;
      for (var name : type.synonyms) {
        if (symbol.equals(name))
          return type;
      }
    }
    return null;
  }

  /**
   * Get the byte order of the given data type string. It tries to identify it
   * from the first character of that type string:
   * <ul>
   *   <li>{@code =} hardware native</li>
   *   <li>{@code <} little-endian</li>
   *   <li>{@code >} big-endian</li>
   *   <li>{@code |} not applicable</li>
   * </ul>
   *
   * @param dtype the data type string (e.g. {@code <i4})
   * @return the identified byte-order or the platform native order if it is
   * not specified in the given type string
   * @see <a href="https://numpy.org/doc/stable/reference/generated/numpy.dtype.byteorder.html">
   * https://numpy.org/doc/stable/reference/generated/numpy.dtype.byteorder.html</a>
   */
  public static ByteOrder byteOrderOf(String dtype) {
    if (dtype == null || dtype.length() == 0)
      return ByteOrder.nativeOrder();
    switch (dtype.charAt(0)) {
      case '>':
        return ByteOrder.BIG_ENDIAN;
      case '<':
        return ByteOrder.LITTLE_ENDIAN;
      default:
        return ByteOrder.nativeOrder();
    }
  }


}
