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

  private T apply() {
    if (array.isDoubleArray()) {
      var a = array.asDoubleArray();
      var data = a.data;
      var swapped = new double[data.length];
      iter((row, col, idx) -> {
        var value = data[idx];
        if (value == 0)
          return;
        var idxSwapped = a.hasFortranOrder()
          ? row * cols + col
          : col * rows + row;
        swapped[idxSwapped] = value;
      });

    }
    return null;
  }

  private void iter(IndexFn fn) {
    int i = 0;
    if (array.hasFortranOrder()) {
      for (int col = 0; col < cols; col++) {
        for (int row = 0; row < rows; row++) {
          fn.accept(row, col, i);
          i++;
        }
      }
    } else {
      for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
          fn.accept(row, col, i);
          i++;
        }
      }
    }
  }

  @FunctionalInterface
  interface IndexFn {
    void accept(int row, int col, int pos);
  }

}
