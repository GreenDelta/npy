package org.openlca.npy.dict;

import org.openlca.npy.DataType;
import org.openlca.npy.Endianness;

/**
 * Contains the values of the dictionary that is stored in the header of an NPY
 * file.
 */
public class Dict {

  private final DataType dataType;
  private final Endianness endianness;
  private final boolean fortranOrder;
  private final int[] shape;

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

  public static class Builder {

  }
}
