package org.openlca.npy;

public class NpyDoubleArray extends NpyArray {

  private final double[] data;

  public NpyDoubleArray(int[] shape, double[] data) {
    super(shape);
    this.data = data;
  }

  public double[] data() {
    return data;
  }

}
