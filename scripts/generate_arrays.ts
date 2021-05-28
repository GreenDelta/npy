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

console.log(generateClass("char"));
