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

    var empty = NpyAsciiType.parse("<i4");
    assertTrue(empty.isEmpty());
  }

}
