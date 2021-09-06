package org.openlca.npy;

import java.math.BigInteger;

import static org.junit.Assert.*;

import org.junit.Test;

public class OrderSwitch2Test {

  @Test
  public void testBigInts() {
    var dataRows = new BigInteger[]{b(1), b(2), b(3), b(4), b(5), b(6)};
    var dataCols = new BigInteger[]{b(1), b(4), b(2), b(5), b(3), b(6)};

    var rowArray = NpyBigIntArray.rowOrderOf(dataRows, 2, 3);
    assertTrue(rowArray.hasRowOrder());

    var colArray = Array2d.switchOrder(rowArray);
    assertTrue(colArray.hasColumnOrder());
    assertArrayEquals(dataCols, colArray.data());
    assertArrayEquals(rowArray.shape(), colArray.shape());

    var rowArray2 = Array2d.switchOrder(colArray);
    assertTrue(rowArray2.hasRowOrder());
    assertArrayEquals(dataRows, rowArray2.data());
    assertArrayEquals(rowArray.shape(), rowArray2.shape());
  }

  private BigInteger b(long value) {
    return BigInteger.valueOf(value);
  }

  @Test
  public void testBooleans() {
    var dataRows = new boolean[]{true, true, true, false, false, false};
    var dataCols = new boolean[]{true, false, true, false, true, false};

    var rowArray = NpyBooleanArray.rowOrderOf(dataRows, 2, 3);
    assertTrue(rowArray.hasRowOrder());

    var colArray = Array2d.switchOrder(rowArray);
    assertTrue(colArray.hasColumnOrder());
    assertArrayEquals(dataCols, colArray.data());
    assertArrayEquals(rowArray.shape(), colArray.shape());

    var rowArray2 = Array2d.switchOrder(colArray);
    assertTrue(rowArray2.hasRowOrder());
    assertArrayEquals(dataRows, rowArray2.data());
    assertArrayEquals(rowArray.shape(), rowArray2.shape());
  }

  @Test
  public void testBytes() {
    var dataRows = new byte[]{1, 2, 3, 4, 5, 6};
    var dataCols = new byte[]{1, 4, 2, 5, 3, 6};

    var rowArray = NpyByteArray.rowOrderOf(dataRows, 2, 3);
    assertTrue(rowArray.hasRowOrder());

    var colArray = Array2d.switchOrder(rowArray);
    assertTrue(colArray.hasColumnOrder());
    assertArrayEquals(dataCols, colArray.data());
    assertArrayEquals(rowArray.shape(), colArray.shape());

    var rowArray2 = Array2d.switchOrder(colArray);
    assertTrue(rowArray2.hasRowOrder());
    assertArrayEquals(dataRows, rowArray2.data());
    assertArrayEquals(rowArray.shape(), rowArray2.shape());
  }

  @Test
  public void testDoubles() {
    var dataRows = new double[]{1, 2, 3, 4, 5, 6};
    var dataCols = new double[]{1, 4, 2, 5, 3, 6};

    var rowArray = NpyDoubleArray.rowOrderOf(dataRows, 2, 3);
    assertTrue(rowArray.hasRowOrder());

    var colArray = Array2d.switchOrder(rowArray);
    assertTrue(colArray.hasColumnOrder());
    assertArrayEquals(dataCols, colArray.data(), 1e-10);
    assertArrayEquals(rowArray.shape(), colArray.shape());

    var rowArray2 = Array2d.switchOrder(colArray);
    assertTrue(rowArray2.hasRowOrder());
    assertArrayEquals(dataRows, rowArray2.data(), 1e-10);
    assertArrayEquals(rowArray.shape(), rowArray2.shape());
  }

  @Test
  public void testFloats() {
    var dataRows = new float[]{1, 2, 3, 4, 5, 6};
    var dataCols = new float[]{1, 4, 2, 5, 3, 6};

    var rowArray = NpyFloatArray.rowOrderOf(dataRows, 2, 3);
    assertTrue(rowArray.hasRowOrder());

    var colArray = Array2d.switchOrder(rowArray);
    assertTrue(colArray.hasColumnOrder());
    assertArrayEquals(dataCols, colArray.data(), 1e-6f);
    assertArrayEquals(rowArray.shape(), colArray.shape());

    var rowArray2 = Array2d.switchOrder(colArray);
    assertTrue(rowArray2.hasRowOrder());
    assertArrayEquals(dataRows, rowArray2.data(), 1e-6f);
    assertArrayEquals(rowArray.shape(), rowArray2.shape());
  }

  @Test
  public void testInts() {
    var dataRows = new int[]{1, 2, 3, 4, 5, 6};
    var dataCols = new int[]{1, 4, 2, 5, 3, 6};

    var rowArray = NpyIntArray.rowOrderOf(dataRows, 2, 3);
    assertTrue(rowArray.hasRowOrder());

    var colArray = Array2d.switchOrder(rowArray);
    assertTrue(colArray.hasColumnOrder());
    assertArrayEquals(dataCols, colArray.data());
    assertArrayEquals(rowArray.shape(), colArray.shape());

    var rowArray2 = Array2d.switchOrder(colArray);
    assertTrue(rowArray2.hasRowOrder());
    assertArrayEquals(dataRows, rowArray2.data());
    assertArrayEquals(rowArray.shape(), rowArray2.shape());
  }

  @Test
  public void testLongs() {
    var dataRows = new long[]{1, 2, 3, 4, 5, 6};
    var dataCols = new long[]{1, 4, 2, 5, 3, 6};

    var rowArray = NpyLongArray.rowOrderOf(dataRows, 2, 3);
    assertTrue(rowArray.hasRowOrder());

    var colArray = Array2d.switchOrder(rowArray);
    assertTrue(colArray.hasColumnOrder());
    assertArrayEquals(dataCols, colArray.data());
    assertArrayEquals(rowArray.shape(), colArray.shape());

    var rowArray2 = Array2d.switchOrder(colArray);
    assertTrue(rowArray2.hasRowOrder());
    assertArrayEquals(dataRows, rowArray2.data());
    assertArrayEquals(rowArray.shape(), rowArray2.shape());
  }

  @Test
  public void testShorts() {
    var dataRows = new short[]{1, 2, 3, 4, 5, 6};
    var dataCols = new short[]{1, 4, 2, 5, 3, 6};

    var rowArray = NpyShortArray.rowOrderOf(dataRows, 2, 3);
    assertTrue(rowArray.hasRowOrder());

    var colArray = Array2d.switchOrder(rowArray);
    assertTrue(colArray.hasColumnOrder());
    assertArrayEquals(dataCols, colArray.data());
    assertArrayEquals(rowArray.shape(), colArray.shape());

    var rowArray2 = Array2d.switchOrder(colArray);
    assertTrue(rowArray2.hasRowOrder());
    assertArrayEquals(dataRows, rowArray2.data());
    assertArrayEquals(rowArray.shape(), rowArray2.shape());
  }

}
