package org.openlca.npy.arrays;

class OrderSwitch2d<T extends NpyArray<?>> {

  private final T array;
  private final int rows;
  private final int cols;

  private OrderSwitch2d(T array) {
    this.array = array;
    this.rows = Array2d.rowCountOf(array);
    this.cols = Array2d.columnCountOf(array);
  }

  static <T extends NpyArray<?>> T of(T array) {
    return new OrderSwitch2d<T>(array).apply();
  }

  @SuppressWarnings("unchecked")
  private T apply() {
    if (array.isDoubleArray()) {
      return (T) switchDoubles(array.asDoubleArray());
    }
    return null;
  }

  private NpyDoubleArray switchDoubles(NpyDoubleArray a) {
    var data = a.data;
    var newData = new double[data.length];
    iter((pos, newPos) -> {
      var value = data[pos];
      if (value == 0)
        return;
      newData[newPos] = value;
    });
    return a.hasColumnOrder()
      ? NpyDoubleArray.rowOrderOf(newData, rows, cols)
      : NpyDoubleArray.columnOrderOf(newData, rows, cols);
  }

  private void iter(IndexFn fn) {
    int pos = 0;
    if (array.hasColumnOrder()) {
      for (int col = 0; col < cols; col++) {
        for (int row = 0; row < rows; row++) {
          int newPos = row * cols + col;
          fn.accept(pos, newPos);
          pos++;
        }
      }
    } else {
      for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
          int newPos = col * rows + row;
          fn.accept(pos, newPos);
          pos++;
        }
      }
    }
  }

  @FunctionalInterface
  interface IndexFn {
    void accept(int pos, int newPos);
  }

}
