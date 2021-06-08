package org.openlca.npy.dict;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.nio.ByteOrder;

import org.junit.Test;
import org.openlca.npy.DataType;
import org.openlca.npy.NpyFormatException;
import org.openlca.npy.NpyHeader;

public class HeaderDictionaryTest {

  @Test
  public void testParse() throws NpyFormatException {
    var a = HeaderDictionary.parse(
      "{'descr': '<i4', 'fortran_order': False, 'shape': (2,), }");
    assertEquals(DataType.i4, a.dataType());
    assertFalse(a.hasFortranOrder());
    assertEquals(1, a.dimensions());
    assertEquals(2, a.sizeOfDimension(0));
    assertEquals(ByteOrder.LITTLE_ENDIAN, a.byteOrder());

    var b = HeaderDictionary.parse(
      "{'descr': '>f8', 'fortran_order': True, 'shape': (4200,2400,), }");
    assertEquals(DataType.f8, b.dataType());
    assertTrue(b.hasFortranOrder());
    assertEquals(2, b.dimensions());
    assertEquals(4200, b.sizeOfDimension(0));
    assertEquals(2400, b.sizeOfDimension(1));
    assertEquals(ByteOrder.BIG_ENDIAN, b.byteOrder());
  }

  @Test
  public void testToString() {
    var dict = HeaderDictionary.of(DataType.i4, new int[]{2, 3})
      .withByteOrder(ByteOrder.LITTLE_ENDIAN)
      .create();
    assertEquals(
      "{'descr': '<i4', 'fortran_order': False, 'shape': (2, 3)}",
      dict.toString());
  }

  @Test
  public void testToStringWithProps() {
    var s = HeaderDictionary.of(DataType.i4, new int[]{2, 3})
      .withByteOrder(ByteOrder.LITTLE_ENDIAN)
      .withOtherProperty("_key", "123abc")
      .create()
      .toString();
    assertEquals("{'descr': '<i4', 'fortran_order': False, " +
      "'shape': (2, 3), '_key': '123abc'}", s);
  }

  @Test
  public void testToNpyHeader() throws Exception {
    var bytes = HeaderDictionary.of(DataType.i4, new int[]{2, 3})
      .withByteOrder(ByteOrder.LITTLE_ENDIAN)
      .withOtherProperty("_key", "123abc")
      .create()
      .toNpyHeader();
    var stream = new ByteArrayInputStream(bytes);
    var header = NpyHeader.read(stream);
    assertEquals(DataType.i4, header.dataType());
  }

}
