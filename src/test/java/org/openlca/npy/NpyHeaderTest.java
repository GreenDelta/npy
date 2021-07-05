package org.openlca.npy;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.RandomAccessFile;

import org.junit.Test;

public class NpyHeaderTest {

  @Test
  public void testReadFromStream() {
    Tests.eachNpy(testNpy -> {
      try (var stream = new FileInputStream(testNpy.file())) {
        var header = NpyHeader.read(stream);
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
        var header = NpyHeader.read(channel);
        checkHeader(testNpy, header);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  private void checkHeader(Tests.TestNpy testNpy, NpyHeader header) {
    var dict = header.dict();
    assertEquals(testNpy.dataType(), dict.dataType());
    if (testNpy.dataType().size() > 1) {
      assertEquals(testNpy.byteOrder(), header.byteOrder());
    }
    assertEquals(testNpy.hasFortranOrder(), dict.hasFortranOrder());
    assertArrayEquals(new int[]{2, 3}, dict.shape());
  }
}
