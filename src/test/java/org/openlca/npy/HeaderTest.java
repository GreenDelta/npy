package org.openlca.npy;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.List;

import org.junit.Test;

public class HeaderTest {

  @Test
  public void testParseHeaders() throws IOException {
    for (var type : DataType.values()) {
      for (var endianness : List.of("be", "le")) {
        for (var order : List.of("c", "f")) {
          testHeaderOf(type, endianness, order);
        }
      }
    }
  }

  private void testHeaderOf(DataType type, String endianness, String order)
    throws IOException {
    var testDir = new File("target/testdata");
    var fileName = type.name() + "_" + endianness + "_" + order + ".npy";
    System.out.println(fileName);
    var file = new File(testDir, fileName);
    try (var stream = new FileInputStream(file)) {
      var header = Header.read(stream);
      var dict = header.dictionary();
      assertEquals(type, dict.dataType());
      assertEquals(order.equals("f"), dict.isInFortranOrder());
      assertEquals(endianness.equals("be") ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN, dict.byteOrder());
    }
  }

}
