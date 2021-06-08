package org.openlca.npy.examples;

import java.io.File;
import java.util.function.Function;
import java.util.function.Supplier;

import org.openlca.npy.Npy;
import org.openlca.npy.arrays.NpyArray;

public class MemmapExample {

  public static void main(String[] args) {
    var file = new File("C:/Users/Win10/openLCA-data-1.4/libraries/ei2_00.00.001/INV.npy");
    System.out.println("test memmap");
    benchmark(() ->  Npy.memmapUnchecked(file), 5);
    System.out.println("test load");
    benchmark(() ->  Npy.loadUnchecked(file), 5);
  }

  private static void benchmark(Supplier<NpyArray<?>> fn, int n) {
    for (int i = 0; i < n; i++) {
      var start = System.nanoTime();
      fn.get();
      var time = ((double) (System.nanoTime() - start)) / 1e9;
      System.out.printf("%d: %.3f seconds %n", i+1, time);
    }
  }

}