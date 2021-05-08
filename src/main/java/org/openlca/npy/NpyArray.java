package org.openlca.npy;

public abstract class NpyArray {

  private final int[] shape;

  protected NpyArray(int[] shape) {
    this.shape = shape;
  }

  public final int[] shape() {
    return shape;
  }

}
