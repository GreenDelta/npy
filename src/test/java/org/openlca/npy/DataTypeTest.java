package org.openlca.npy;

import static org.junit.Assert.*;

import java.nio.ByteOrder;

import org.junit.Test;

public class DataTypeTest {

  @Test
  public void testByteOrder() {
    var dtypes = new String[] {
      null,
      "",
      "i4",
      "=i4",
      "<i4",
      ">i4",
      "|?"
    };
    var expected = new ByteOrder[] {
      ByteOrder.nativeOrder(),
      ByteOrder.nativeOrder(),
      ByteOrder.nativeOrder(),
      ByteOrder.nativeOrder(),
      ByteOrder.LITTLE_ENDIAN,
      ByteOrder.BIG_ENDIAN,
      ByteOrder.nativeOrder(),
    };
    for (int i = 0; i < dtypes.length; i++) {
      assertEquals(expected[i], DataType.byteOrderOf(dtypes[i]));
    }
  }

  @Test
  public void testGetType() {

    var expected = new Expected[] {

      Expected.of(DataType.bool, 1, new String[] {
        "|b1",
        "?",
        "b1",
        "bool",
        "bool8",
        "bool_",
      }),

      Expected.of(DataType.i1, 1, new String[] {
        "|i1",
        "b",
        "byte",
        "i1",
        "int8",
      }),

      Expected.of(DataType.u1, 1, new String[] {
        "|u1",
        "B",
        "u1",
        "ubyte",
        "uint8",
      }),

      Expected.of(DataType.i2, 2, new String[] {
        "<i2",
        "h",
        "i2",
        "int16",
        "short",
      }),

    };

    for (var e : expected) {
      for (var symbol : e.symbols) {
        var dtype = DataType.of(symbol);
        assertEquals(e.type, dtype);
        assertEquals(e.size, dtype.size());
      }
    }

  }

  static private class Expected {
    final DataType type;
    final int size;
    final String[] symbols;

    Expected(DataType type, int size, String[] symbols) {
      this.type = type;
      this.size = size;
      this.symbols = symbols;
    }

    static Expected of(DataType type, int size, String[] symbols) {
      return new Expected(type, size, symbols);
    }
  }

}
