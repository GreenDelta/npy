package org.openlca.npy;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import org.junit.Test;
import org.openlca.npy.dict.HeaderDictionary;

public class NpyReadStringTest {

  @Test
  public void testReadStringFromChannel() throws Exception {
    var s = this.getClass().getResourceAsStream("scipy_csc_format.npy");
    try (var stream = Objects.requireNonNull(s);
         var channel = Channels.newChannel(stream)) {
      var string = Npy.readString(channel);
      assertEquals("csc", string);
    }
  }

  @Test
  public void testReadStringFromFile() throws Exception {
    var temp = Files.createTempFile("scipy_csc_format", ".npy");
    var s = this.getClass().getResourceAsStream("scipy_csc_format.npy");
    try (var stream = Objects.requireNonNull(s)) {
      Files.copy(stream, temp, StandardCopyOption.REPLACE_EXISTING);
      var string = Npy.readString(temp.toFile());
      assertEquals("csc", string);
    }
  }

  @Test
  public void testReadNullTerminated() throws Exception {
    var file = Files.createTempFile("str_", ".npy").toFile();
    var dict = HeaderDictionary.of(NpyDataType.S).create();
    var data = new byte[]{'c', 's', 'c', 0};
    Npy.write(file, dict, data);
    assertEquals("csc", Npy.readString(file));
    try (var stream = new FileInputStream(file);
         var channel = Channels.newChannel(stream)) {
      assertEquals("csc", Npy.readString(channel));
    }
    Files.delete(file.toPath());
  }

}
