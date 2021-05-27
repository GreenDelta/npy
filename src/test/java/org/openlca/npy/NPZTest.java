package org.openlca.npy;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class NPZTest {

  @Test
  public void readU8() throws Exception {
    Tests.eachNpy(testNpy -> {
      try (var file = new RandomAccessFile(testNpy.file(), "r");
           var channel = file.getChannel()) {
        var header = Header.read(channel);

        var dataSize = 2 * 3 * testNpy.dataType().size();

        var buffer = channel.map(
          FileChannel.MapMode.READ_ONLY,
          header.dataOffset(),
          dataSize);
        buffer.order(testNpy.byteOrder());
        buffer.asDoubleBuffer().array();
        buffer.asFloatBuffer();

      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
