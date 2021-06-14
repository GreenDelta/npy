package org.openlca.npy;

import static org.junit.Assert.*;

import java.nio.ByteOrder;

import org.junit.Test;

public class DataTypeTest {

  @Test
  public void testByteOrder() {
    var dtypes = new String[]{
      null,
      "",
      "i4",
      "=i4",
      "<i4",
      ">i4",
      "|?"
    };
    var expected = new ByteOrder[]{
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

    var expected = new Expected[]{

      Expected.of(DataType.bool, 1, new String[]{
        "|b1",
        "?",
        "b1",
        "bool",
        "bool8",
        "bool_",
      }),

      Expected.of(DataType.i1, 1, new String[]{
        "|i1",
        "b",
        "byte",
        "i1",
        "int8",
      }),

      Expected.of(DataType.u1, 1, new String[]{
        "|u1",
        "B",
        "u1",
        "ubyte",
        "uint8",
      }),

      Expected.of(DataType.i2, 2, new String[]{
        "<i2",
        "h",
        "i2",
        "int16",
        "short",
      }),

      Expected.of(DataType.u2, 2, new String[]{
        "<u2",
        "H",
        "u2",
        "uint16",
        "ushort",
      }),

      Expected.of(DataType.i4, 4, new String[]{
        "<i4",
        "i",
        "i4",
        "int",
        "int32",
        "int_",
        "intc",
        "l",
        "long",
      }),

      Expected.of(DataType.i4, 4, new String[]{
        "<i4",
        "i",
        "i4",
        "int",
        "int32",
        "int_",
        "intc",
        "l",
        "long",
      }),

      Expected.of(DataType.u4, 4, new String[]{
        "<u4",
        "I",
        "L",
        "u4",
        "uint",
        "uint32",
        "uintc",
      }),

      Expected.of(DataType.i8, 8, new String[]{
        "<i8",
        "i8",
        "int0",
        "int64",
        "intp",
        "longlong",
        "p",
        "q",
      }),

      Expected.of(DataType.u8, 8, new String[]{
        "<u8",
        "P",
        "Q",
        "Uint64",
        "u8",
        "uint0",
        "uint64",
        "uintp",
        "ulonglong",
      }),

      Expected.of(DataType.f2, 2, new String[]{
        "<f2",
        "e",
        "f2",
        "float16",
        "half",
      }),

      Expected.of(DataType.f4, 4, new String[]{
        "<f4",
        "f",
        "f4",
        "float32",
        "single",
      }),

      Expected.of(DataType.f8, 8, new String[]{
        "<f8",
        "d",
        "double",
        "f8",
        "float",
        "float64",
        "float_",
        "g",
        "longdouble",
        "longfloat",
      }),

      Expected.of(DataType.S, 0, new String[]{
        "|S0",
        "Bytes0",
        "S",
        "a",
        "bytes",
        "bytes0",
        "bytes_",
        "string_",
      }),

      Expected.of(DataType.U, 0, new String[]{
        "<U0",
        "Str0",
        "U",
        "str",
        "str0",
        "str_",
        "unicode",
        "unicode_",
      })
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
