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

console.log(generateArray2dDMethods("short"));
