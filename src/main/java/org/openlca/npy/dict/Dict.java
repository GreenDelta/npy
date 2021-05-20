package org.openlca.npy.dict;

import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import org.openlca.npy.DataType;
import org.openlca.npy.UnsupportedFormatException;

/**
 * Contains the values of the dictionary that is stored in the header of an NPY
 * file.
 */
public class Dict {

  private final DataType dataType;
  private final ByteOrder byteOrder;
  private final boolean fortranOrder;
  private final int[] shape;

  private final Map<String, String> properties = new HashMap<>();

  public Dict(DataType dataType,
              ByteOrder byteOrder,
              boolean fortranOrder,
              int[] shape) {
    this.dataType = dataType;
    this.byteOrder = byteOrder;
    this.fortranOrder = fortranOrder;
    this.shape = shape;
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

  public int[] shape() {
    return shape;
  }

  public static Dict parse(String s) {
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

}
