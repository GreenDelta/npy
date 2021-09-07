package org.openlca.npy;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import org.junit.Test;
import org.openlca.npy.dict.NpyHeaderDict;

public class NpyStringTest {

  @Test
  public void testReadStringFromChannel() throws Exception {
    var s = this.getClass().getResourceAsStream("scipy_csc_format.npy");
    try (var stream = Objects.requireNonNull(s);
         var channel = Channels.newChannel(stream)) {
      var array = Npy.read(channel);
      assertTrue(array.isCharArray());
      assertEquals("csc", array.toString());
    }
  }

  @Test
  public void testReadStringFromFile() {
    Tests.withFile(file -> {
        var s = this.getClass().getResourceAsStream("scipy_csc_format.npy");
        try (var stream = Objects.requireNonNull(s)) {
          Files.copy(stream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
          var array = Npy.read(file);
          assertTrue(array.isCharArray());
          assertEquals("csc", array.toString());
        }
      }
    );
  }

  @Test
  public void testReadNullTerminated() {
    Tests.withFile(file -> {
      var dict = NpyHeaderDict.of(NpyDataType.S)
        .withTypeSize(3)
        .create();
      var data = new byte[]{'c', 's', 'c', 0};
      Npy.write(file, dict, data);
      assertEquals("csc", Npy.read(file).toString());
      try (var stream = new FileInputStream(file);
           var channel = Channels.newChannel(stream)) {
        assertEquals("csc", Npy.read(channel).toString());
      }
    });
  }

  @Test
  public void testWriteAsciiArray() {
    Tests.withFile(file -> {
      var ascii = NpyCharArray.of("csc");
      assertEquals(NpyDataType.S, ascii.dataType());
      Npy.write(file, ascii);
      var array = Npy.read(file);
      assertTrue(array.isCharArray());
      assertEquals("csc", array.toString());
    });
  }

  @Test
  public void testWriteUnicodeArray() {
    Tests.withFile(file -> {
      var unicode = NpyCharArray.of("ÁléImÜberflög");
      assertEquals(NpyDataType.U, unicode.dataType());
      Npy.write(file, unicode);
      var array = Npy.read(file);
      assertTrue(array.isCharArray());
      assertEquals("ÁléImÜberflög", array.toString());
    });
  }

}
