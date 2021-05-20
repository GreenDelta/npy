package org.openlca.npy.dict;

import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.openlca.npy.DataType;
import org.openlca.npy.UnsupportedFormatException;

/**
 * Contains the values of the dictionary that is stored in the header of an NPY
 * file.
 */
public class HeaderDictionary {

  private final DataType dataType;
  private final ByteOrder byteOrder;
  private final boolean fortranOrder;
  private final int[] shape;
  private final Map<String, String> properties;

  public HeaderDictionary(Builder builder) {
    this.dataType = builder.dataType;
    this.shape = Arrays.copyOf(builder.dimensions, builder.dimensions.length);
    this.byteOrder = builder.byteOrder == null
      ? ByteOrder.nativeOrder()
      : builder.byteOrder;
    this.fortranOrder = builder.fortranOrder;
    this.properties = builder.properties != null
      ? builder.properties
      : Collections.emptyMap();
  }

  public static Builder of(DataType dataType, int[] dimensions) {
    return new Builder(dataType, dimensions);
  }

  public DataType dataType() {
    return dataType;
  }

  public ByteOrder byteOrder() {
    return byteOrder;
  }

  public boolean isInFortranOrder() {
    return fortranOrder;
  }

  /**
   * Returns the number of dimensions of the array..
   */
  public int dimensions() {
    return shape.length;
  }

  /**
   * Returns the size if the ith dimension of the array.
   *
   * @param i the 0-based dimension for which the size is requested.
   * @return the size of the requested dimension
   * @throws IndexOutOfBoundsException if {@code i < 0 || i >= dimensions()}
   */
  public int sizeOfDimension(int i) {
    if (i < 0 || i >= shape.length)
      throw new IndexOutOfBoundsException(i);
    return shape[i];
  }

  public Map<String, String> otherProperties() {
    return properties.isEmpty()
      ? Collections.emptyMap()
      : Collections.unmodifiableMap(properties);
  }

  public static HeaderDictionary parse(String s) {
    var value = Parser.parse(s);
    if (value.isError())
      throw new UnsupportedFormatException(
        "invalid header dictionary: " + value.asError().message());
    if (!value.isDict())
      throw new UnsupportedFormatException(
        "invalid header dictionary; type is " + value.getClass());

    var dict = value.asDict();

    var typeEntry = dict.get("descr");
    if (typeEntry.isNone())
      throw new UnsupportedFormatException(
        "invalid header dictionary; data type field 'descr' is missing");
    if (!typeEntry.isString())
      throw new UnsupportedFormatException(
        "invalid header dictionary; data type field " +
        "'descr' is not a string but: " + typeEntry);


    return null;
  }

  private static boolean getFortranOrder(PyDict dict) {
    var entry = dict.get("fortran_order");
    if (entry.isNone())
      return false;
    if (!entry.isIdentifier())
      throw new UnsupportedFormatException(
        "invalid header dictionary: fortran_order must be " +
        "True or False but was '" + entry + "'");
    var value = entry.asIdentifier().value();
    switch (value) {
      case "True":
        return true;
      case "False":
        return false;
      default:
        throw new UnsupportedFormatException(
          "invalid header dictionary: fortran_order must be " +
          "True or False but was '" + value + "'");
    }
  }

  private static ByteOrder getByteOrder(String dtype) {
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

  private static DataType getDataType(String dtype) {
    if (dtype == null || dtype.length() == 0)
      throw new UnsupportedFormatException(
        "invalid header dictionary; data type field 'descr' is an empty string");
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
    }
    throw new UnsupportedFormatException(
      "unsupported data type: '" + symbol + "'");
  }

  public static class Builder {

    private final DataType dataType;
    private final int[] dimensions;
    private ByteOrder byteOrder;
    private boolean fortranOrder;
    private Map<String, String> properties;

    private Builder(DataType dataType, int[] dimensions) {
      this.dataType = Objects.requireNonNull(dataType);
      this.dimensions = Objects.requireNonNull(dimensions);
      if (dimensions.length == 0)
        throw new IllegalArgumentException("no dimensions");
    }

    public Builder withByteOrder(ByteOrder byteOrder) {
      this.byteOrder = byteOrder;
      return this;
    }

    public Builder withFortranOrder(boolean b) {
      this.fortranOrder = b;
      return this;
    }

    public Builder withOtherProperty(String key, String value) {
      if (properties == null) {
        properties = new HashMap<>();
      }
      properties.put(key, value);
      return this;
    }
  }
}
