package org.openlca.npy;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.junit.Test;

public class HeaderTest {

  @Test
  public void testReadFromStream() {
    Tests.eachNpy(testNpy -> {
      try (var stream = new FileInputStream(testNpy.file())) {
        var header = Header.read(stream);
        checkHeader(testNpy, header);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Test
  public void testReadFromChannel() {
    Tests.eachNpy(testNpy -> {
      try (var file = new RandomAccessFile(testNpy.file(), "r");
           var channel = file.getChannel()) {
        var header = Header.read(channel);
        checkHeader(testNpy, header);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  private void checkHeader(Tests.TestNpy testNpy, Header header) {
    var dict = header.dictionary();
    assertEquals(testNpy.dataType(), dict.dataType());
    if (testNpy.dataType().size() > 1) {
      assertEquals(testNpy.byteOrder(), dict.byteOrder());
    }
    assertEquals(testNpy.hasFortranOrder(), dict.hasFortranOrder());
    assertEquals(2, dict.dimensions());
    assertEquals(2, dict.sizeOfDimension(0));
    assertEquals(3, dict.sizeOfDimension(1));
  }
}
