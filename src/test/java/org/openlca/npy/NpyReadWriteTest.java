package org.openlca.npy;

import java.math.BigInteger;
import java.nio.file.Files;

import static org.junit.Assert.*;

import org.junit.Test;

public class NpyReadWriteTest {

  @Test
  public void testBigInts() throws Exception {
    var array = new NpyBigIntArray(
      new int[]{2, 3},
      new BigInteger[]{
        BigInteger.valueOf(1),
        BigInteger.valueOf(2),
        BigInteger.valueOf(3),
        BigInteger.valueOf(4),
        BigInteger.valueOf(5),
        BigInteger.valueOf(6)},
      true);
    var tempFile = Files.createTempFile("_npy_test", ".npy").toFile();
    Npy.write(tempFile, array);
    var copy = Npy.read(tempFile);
    assertTrue(copy.isBigIntegerArray());
    assertTrue(copy.hasColumnOrder());
    assertArrayEquals(array.shape(), copy.shape());
    assertArrayEquals(array.data(), ((NpyBigIntArray) copy).data());
    Files.delete(tempFile.toPath());
  }

  @Test
  public void testBooleans() throws Exception {
    var array = new NpyBooleanArray(
      new int[]{2, 3},
      new boolean[]{true, false, true, false, true, false},
      true);
    var tempFile = Files.createTempFile("_npy_test", ".npy").toFile();
    Npy.write(tempFile, array);
    var copy = Npy.read(tempFile);
    assertTrue(copy.isBooleanArray());
    assertTrue(copy.hasColumnOrder());
    assertArrayEquals(array.shape(), copy.shape());
    assertArrayEquals(array.data(), copy.asBooleanArray().data());
    Files.delete(tempFile.toPath());
  }

  @Test
  public void testBytes() throws Exception {
    var array = new NpyByteArray(
      new int[]{2, 3},
      new byte[]{1, 2, 3, 4, 5, 6},
      false);
    var tempFile = Files.createTempFile("_npy_test", ".npy").toFile();
    Npy.write(tempFile, array);
    var copy = Npy.read(tempFile);
    assertTrue(copy.isByteArray());
    assertFalse(copy.hasColumnOrder());
    assertArrayEquals(array.shape(), copy.shape());
    assertArrayEquals(array.data(), copy.asByteArray().data());
    Files.delete(tempFile.toPath());
  }

  @Test
  public void testDoubles() throws Exception {
    var array = new NpyDoubleArray(
      new int[]{2, 3},
      new double[]{1, 2, 3, 4, 5, 6},
      true);
    var tempFile = Files.createTempFile("_npy_test", ".npy").toFile();
    Npy.write(tempFile, array);
    var copy = Npy.read(tempFile);
    assertTrue(copy.isDoubleArray());
    assertTrue(copy.hasColumnOrder());
    assertArrayEquals(array.shape(), copy.shape());
    assertArrayEquals(array.data(), copy.asDoubleArray().data(), 1e-16);
    Files.delete(tempFile.toPath());
  }

  @Test
  public void testFloats() throws Exception {
    var array = new NpyFloatArray(
      new int[]{2, 3},
      new float[]{1, 2, 3, 4, 5, 6},
      false);
    var tempFile = Files.createTempFile("_npy_test", ".npy").toFile();
    Npy.write(tempFile, array);
    var copy = Npy.read(tempFile);
    assertTrue(copy.isFloatArray());
    assertFalse(copy.hasColumnOrder());
    assertArrayEquals(array.shape(), copy.shape());
    assertArrayEquals(array.data(), copy.asFloatArray().data(), 1e-16f);
    Files.delete(tempFile.toPath());
  }

  @Test
  public void testInts() throws Exception {
    var array = new NpyIntArray(
      new int[]{2, 3},
      new int[]{1, 2, 3, 4, 5, 6},
      true);
    var tempFile = Files.createTempFile("_npy_test", ".npy").toFile();
    Npy.write(tempFile, array);
    var copy = Npy.read(tempFile);
    assertTrue(copy.isIntArray());
    assertTrue(copy.hasColumnOrder());
    assertArrayEquals(array.shape(), copy.shape());
    assertArrayEquals(array.data(), copy.asIntArray().data());
    Files.delete(tempFile.toPath());
  }

  @Test
  public void testLongs() throws Exception {
    var array = new NpyLongArray(
      new int[]{2, 3},
      new long[]{1, 2, 3, 4, 5, 6},
      false);
    var tempFile = Files.createTempFile("_npy_test", ".npy").toFile();
    Npy.write(tempFile, array);
    var copy = Npy.read(tempFile);
    assertTrue(copy.isLongArray());
    assertFalse(copy.hasColumnOrder());
    assertArrayEquals(array.shape(), copy.shape());
    assertArrayEquals(array.data(), copy.asLongArray().data());
    Files.delete(tempFile.toPath());
  }

  @Test
  public void testShorts() throws Exception {
    var array = new NpyShortArray(
      new int[]{2, 3},
      new short[]{1, 2, 3, 4, 5, 6},
      true);
    var tempFile = Files.createTempFile("_npy_test", ".npy").toFile();
    Npy.write(tempFile, array);
    var copy = Npy.read(tempFile);
    assertTrue(copy.isShortArray());
    assertTrue(copy.hasColumnOrder());
    assertArrayEquals(array.shape(), copy.shape());
    assertArrayEquals(array.data(), copy.asShortArray().data());
    Files.delete(tempFile.toPath());
  }

}
