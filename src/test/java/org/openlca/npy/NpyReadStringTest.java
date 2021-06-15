package org.openlca.npy;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import org.junit.Test;

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
  public void testNULL() throws Exception {
    // var stream = getClass().getResourceAsStream("scipy_csc_format.npy");
    
    var stream = new FileInputStream("C:/Users/ms/Projects/dev/npy/src/test/resources/org/openlca/npy/scipy_csc_format.npy");
    
    assertNotNull(stream);
    var bytes = new byte[1024];
    int n = stream.read(bytes);
    for (int i = 0; i < n; i++) {
      System.out.println(i + " :: " + (char) bytes[i]);
    }

    System.out.println(new String(bytes, n - 4, 4, StandardCharsets.UTF_8));
    stream.close();
  }

}
