package org.openlca.npy;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import org.junit.Test;
import org.openlca.npy.dict.NpyHeaderDict;

public class NpyReadStringTest {

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
  public void testReadStringFromFile() throws Exception {
    var temp = Files.createTempFile("scipy_csc_format", ".npy");
    var s = this.getClass().getResourceAsStream("scipy_csc_format.npy");
    try (var stream = Objects.requireNonNull(s)) {
      Files.copy(stream, temp, StandardCopyOption.REPLACE_EXISTING);
      var array = Npy.read(temp.toFile());
      assertTrue(array.isCharArray());
      assertEquals("csc", array.toString());
    }
  }

  @Test
  public void testReadNullTerminated() throws Exception {
    var file = Files.createTempFile("str_", ".npy").toFile();
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
    Files.delete(file.toPath());
  }

}
