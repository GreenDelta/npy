package org.openlca.npy;

import static org.junit.Assert.*;

import java.io.FileInputStream;

import org.junit.Test;

public class HeaderTest {

  @Test
  public void testParseHeaders() {
    Tests.eachNpy(testNpy -> {
      try (var stream = new FileInputStream(testNpy.file())) {
        var header = Header.read(stream);
        var dict = header.dictionary();
        assertEquals(testNpy.dataType(), dict.dataType());
        if (testNpy.dataType().size() > 1) {
          assertEquals(testNpy.byteOrder(), dict.byteOrder());
        }
        assertEquals(testNpy.hasFortranOrder(), dict.isInFortranOrder());
        assertEquals(2, dict.dimensions());
        assertEquals(2, dict.sizeOfDimension(0));
        assertEquals(3, dict.sizeOfDimension(1));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }
}
