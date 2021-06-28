package org.openlca.npy;

import static org.junit.Assert.*;

import org.junit.Test;

public class NpyStringTypeTest {

  @Test
  public void testParseAscii() {
    var a42 = NpyAsciiType.parse("a42");
    assertTrue(a42.isPresent());
    assertEquals(42, a42.get().size());

    var bytes0 = NpyAsciiType.parse("bytes0");
    assertTrue(bytes0.isPresent());
    assertEquals(0, bytes0.get().size());

    var s3 = NpyAsciiType.parse("|S3");
    assertTrue(s3.isPresent());
    assertEquals(3, s3.get().size());

    var s0 = NpyAsciiType.parse("S");
    assertTrue(s0.isPresent());
    assertEquals(0, s0.get().size());

    var empty = NpyAsciiType.parse("<i4");
    assertTrue(empty.isEmpty());
  }

  @Test
  public void testParseUnicode() {
    var str42 = NpyUnicodeType.parse("str42");
    assertTrue(str42.isPresent());
    assertEquals(42, str42.get().size());

    var unicode0 = NpyUnicodeType.parse("unicode0");
    assertTrue(unicode0.isPresent());
    assertEquals(0, unicode0.get().size());

    var u3 = NpyUnicodeType.parse("|U3");
    assertTrue(u3.isPresent());
    assertEquals(3, u3.get().size());

    var u0 = NpyUnicodeType.parse("U");
    assertTrue(u0.isPresent());
    assertEquals(0, u0.get().size());

    var empty = NpyUnicodeType.parse("<i4");
    assertTrue(empty.isEmpty());
  }

}
