package org.openlca.npy.dict;

import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import org.openlca.npy.DataType;
import org.openlca.npy.Endianness;
import org.openlca.npy.UnsupportedFormatException;

/**
 * Contains the values of the dictionary that is stored in the header of an NPY
 * file.
 */
public class Dict {

  private final DataType dataType;
  private final Endianness endianness;
  private final boolean fortranOrder;
  private final int[] shape;

  private final Map<String, String> properties = new HashMap<>();

  public Dict(DataType dataType,
              Endianness endianness,
              boolean fortranOrder,
              int[] shape) {
    this.dataType = dataType;
    this.endianness = endianness;
    this.fortranOrder = fortranOrder;
    this.shape = shape;
  }

  public DataType dataType() {
    return dataType;
  }

  public Endianness endianness() {
    return endianness;
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

}
