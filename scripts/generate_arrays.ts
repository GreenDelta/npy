const PRIMITIVES = [
  "boolean",
  "byte",
  "char",
  "double",
  "float",
  "int",
  "long",
  "shor",
];

function generateClass(type: string): string {
  const uName = type[0].toUpperCase() + type.substring(1);
  const text = `
  package org.openlca.npy.arrays;

  public final class Npy${uName}Array extends AbstractNpyArray<${type}[]> {
  
    public Npy${uName}Array(int[] shape, ${type}[] data, boolean fortranOrder) {
      super(shape, data, fortranOrder);
    }
  
    @Override
    public int size() {
      return data.length;
    }
  
    /*
    @Override
    public boolean is${uName}Array() {
      return true;
    }
  
    @Override
    public Npy${uName}Array as${uName}Array() {
      return this;
    }
    */
  
    @Override
    public NpyDoubleArray asDoubleArray() {
      var doubles = new double[data.length];
      for (int i = 0; i < data.length; i++) {
        doubles[i] = data[i];
      }
      return new NpyDoubleArray(copyShape(), doubles, fortranOrder);
    }

    @Override
    public NpyFloatArray asFloatArray() {
      var floats = new float[data.length];
      for (int i = 0; i < data.length; i++) {
        floats[i] = (float) data[i];
      }
      return new NpyFloatArray(copyShape(), floats, fortranOrder);
    }
  } 
  `;
  return text;
}

function generateArray2dDMethods(type: string): string {
  const Type = type[0].toUpperCase() + type.substring(1);
  return `
  public static ${type} get(Npy${Type}Array array, int row, int col) {
    int i = indexOf(array, row, col);
    return array.data[i];
  }

  public static ${type}[] getRow(Npy${Type}Array array, int row) {
    int cols = array.shape[1];
    if (!array.hasFortranOrder()) {
      int offset = row * cols;
      return Arrays.copyOfRange(array.data, offset, offset + cols);
    }
    int rows = array.shape[0];
    ${type}[] values = new ${type}[cols];
    int offset = 0;
    for (int col = 0; col < cols; col++) {
      values[col] = array.data[offset + row];
      offset += rows;
    }
    return values;
  }

  public static ${type}[] getColumn(Npy${Type}Array array, int col) {
    int rows = array.shape[0];
    if (array.hasFortranOrder()) {
      int offset = col * rows;
      return Arrays.copyOfRange(array.data, offset, offset + rows);
    }
    int cols = array.shape[1];
    ${type}[] values = new ${type}[rows];
    int offset = 0;
    for (int row = 0; row < rows; row++) {
      values[row] = array.data[offset + col];
      offset += cols;
    }
    return values;
  }
  `;
}

function generateChecks(type: string): string {
  const Type = type[0].toUpperCase() + type.substring(1);
  return `
  static void check${Type}s(Npy${Type}Array array) {

    assertTrue(Array2D.isValid(array));
    assertEquals(2, Array2D.rowCountOf(array));
    assertEquals(3, Array2D.columnCountOf(array));

    // check each element
    var expected = new ${type}[][]{
      new ${type}[]{0, 1, 2},
      new ${type}[]{3, 4, 5},
    };
    for (int row = 0; row < 2; row++) {
      for (int col = 0; col < 3; col++) {
        assertEquals(expected[row][col], Array2D.get(array, row, col), 1e-16);
      }
    }

    // check by rows
    assertArrayEquals(expected[0], Array2D.getRow(array, 0), 1e-16);
    assertArrayEquals(expected[1], Array2D.getRow(array, 1), 1e-16);

    // check by columns
    assertArrayEquals(new ${type}[]{0, 3}, Array2D.getColumn(array, 0), 1e-16);
    assertArrayEquals(new ${type}[]{1, 4}, Array2D.getColumn(array, 1), 1e-16);
    assertArrayEquals(new ${type}[]{2, 5}, Array2D.getColumn(array, 2), 1e-16);

    // check by storage order
    if (array.hasFortranOrder()) {
      assertArrayEquals(new ${type}[]{0, 3, 1, 4, 2, 5}, array.data(), 1e-16);
    } else {
      assertArrayEquals(new ${type}[]{0, 1, 2, 3, 4, 5}, array.data(), 1e-16);
    }
  }
  `;
}

console.log(generateChecks("int"));
