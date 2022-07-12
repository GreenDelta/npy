package org.openlca.npy.dict;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.nio.ByteOrder;

import org.junit.Test;
import org.openlca.npy.NpyByteOrder;
import org.openlca.npy.NpyDataType;
import org.openlca.npy.NpyFormatException;
import org.openlca.npy.NpyHeader;

public class NpyHeaderDictTest {

  @Test
  public void testParse() throws NpyFormatException {
    var a = NpyHeaderDict.parse(
      "{'descr': '<i4', 'fortran_order': False, 'shape': (2,), }");
    assertEquals(NpyDataType.i4, a.dataType());
    assertFalse(a.hasFortranOrder());
    assertEquals(1, a.dimensions());
    assertEquals(2, a.sizeOfDimension(0));
    assertEquals(NpyByteOrder.LITTLE_ENDIAN, a.byteOrder());
    assertEquals(4, a.typeSize());

    var b = NpyHeaderDict.parse(
      "{'descr': '>f8', 'fortran_order': True, 'shape': (4200,2400,), }");
    assertEquals(NpyDataType.f8, b.dataType());
    assertTrue(b.hasFortranOrder());
    assertEquals(2, b.dimensions());
    assertEquals(4200, b.sizeOfDimension(0));
    assertEquals(2400, b.sizeOfDimension(1));
    assertEquals(NpyByteOrder.BIG_ENDIAN, b.byteOrder());
    assertEquals(8, b.typeSize());
  }

  @Test
  public void testToString() {
    var dict = NpyHeaderDict.of(NpyDataType.i4)
      .withShape(new int[]{2, 3})
      .withByteOrder(NpyByteOrder.LITTLE_ENDIAN)
      .create();
    assertEquals(
      "{'descr': '<i4', 'fortran_order': False, 'shape': (2, 3,)}",
      dict.toString());
  }

  @Test
  public void testToStringWithProps() {
    var s = NpyHeaderDict.of(NpyDataType.i4)
      .withShape(new int[]{2, 3})
      .withByteOrder(NpyByteOrder.LITTLE_ENDIAN)
      .withOtherProperty("_key", "123abc")
      .create()
      .toString();
    assertEquals("{'descr': '<i4', 'fortran_order': False, " +
      "'shape': (2, 3,), '_key': '123abc'}", s);
  }

  @Test
  public void testToNpyHeader() throws Exception {
    var bytes = NpyHeaderDict.of(NpyDataType.i4)
      .withShape(new int[]{2, 3})
      .withByteOrder(NpyByteOrder.LITTLE_ENDIAN)
      .withFortranOrder(true)
      .withOtherProperty("_key", "123abc")
      .create()
      .toNpyHeader();
    var stream = new ByteArrayInputStream(bytes);
    var header = NpyHeader.read(stream);
    var dict = header.dict();
    assertEquals(NpyDataType.i4, dict.dataType());
    assertTrue(dict.hasFortranOrder());
    assertEquals(ByteOrder.LITTLE_ENDIAN, header.byteOrder());
    assertEquals(2 * 3 * 4, dict.dataSize());
    assertEquals(2 * 3, dict.numberOfElements());
    assertEquals("123abc", dict.property("_key"));
    assertArrayEquals(new int[] {2, 3}, dict.shape());
    assertEquals(0, header.dataOffset() % 64);
  }

  @Test
  public void testNonAsciiToNpyHeader() throws Exception {
    var bytes = NpyHeaderDict.of(NpyDataType.i4)
      .withShape(new int[]{2, 3})
      .withByteOrder(NpyByteOrder.LITTLE_ENDIAN)
      .withFortranOrder(true)
      .withOtherProperty("_key", "Überstraße")
      .create()
      .toNpyHeader();

    var stream = new ByteArrayInputStream(bytes);
    var header = NpyHeader.read(stream);
    var dict = header.dict();
    assertEquals(NpyDataType.i4, dict.dataType());
    assertTrue(dict.hasFortranOrder());
    assertEquals(ByteOrder.LITTLE_ENDIAN, header.byteOrder());
    assertEquals(2 * 3 * 4, dict.dataSize());
    assertEquals(2 * 3, dict.numberOfElements());
    assertEquals("Überstraße", dict.property("_key"));
    assertArrayEquals(new int[] {2, 3}, dict.shape());
    assertEquals(0, header.dataOffset() % 64);
  }

  @Test
  public void testStringSize() {

    var str = NpyHeaderDict.of(NpyDataType.S)
      .withTypeSize(42)
      .create()
      .toString();
    assertEquals("{'descr': '|S42', 'fortran_order': False, 'shape': ()}", str);

    var dict = NpyHeaderDict.parse(str);
    assertEquals(NpyDataType.S, dict.dataType());
    assertEquals(NpyByteOrder.NOT_APPLICABLE, dict.byteOrder());
    assertEquals(42, dict.typeSize());
    assertEquals(0, dict.dimensions());
  }
}
