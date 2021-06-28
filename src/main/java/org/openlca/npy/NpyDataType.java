package org.openlca.npy;

public interface NpyDataType {

  /**
   * Get the NPY symbol of the data type.
   *
   * @return the NPY symbol of the data type, e.g. {@code i4} or {@code S3}
   */
  String symbol();

  /**
   * Get the size of the data type in number of bytes. This is similar to
   * {@code numpy.dtype.itemsize}.
   *
   * @return the size of the data type in number of bytes.
   */
  int size();

}
