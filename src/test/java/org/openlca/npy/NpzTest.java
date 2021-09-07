package org.openlca.npy;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import java.io.File;
import java.nio.file.Files;

import org.junit.Test;

public class NpzTest {

  @Test
  public void testNpzEntries() {
    var npz = new File(Tests.testDir, "all.npz");
    assumeTrue(npz.exists());
    var entries = Npz.entries(npz);
    Tests.eachNpy(npy -> {
      var entry = npy.file().getName();
      assertTrue(entries.contains(entry));
    });
  }

  @Test
  public void testReadNpz() {
    var npz = new File(Tests.testDir, "all.npz");
    assumeTrue(npz.exists());
    Tests.eachNpy(npy -> {
      var entry = npy.file().getName();
      var array = Npz.read(npz, entry);
      Tests.check(npy, array);
    });
  }

  @Test
  public void testUseNpz() {
    var file = new File(Tests.testDir, "all.npz");
    assumeTrue(file.exists());
    Npz.use(file, npz -> {
      for (var entry : Npz.entries(npz)) {
        var array = Npz.read(npz, entry);
        if (array.dataType().size() != 0) {
          assertEquals(6, array.size());
        }
      }
    });
  }

  @Test
  public void testCreateWriteNpz() throws Exception {
    var temp = Files.createTempFile("_npy_tests", ".npz").toFile();
    Npz.create(temp, npz -> Tests.eachNpy(npy -> {
      var entry = npy.file().getName();
      var array = Npy.read(npy.file());
      Npz.write(npz, entry, array);
    }));

    Tests.eachNpy(npy -> {
      var entry = npy.file().getName();
      var array = Npz.read(temp, entry);
      Tests.check(npy, array);
    });
    Files.delete(temp.toPath());
  }

  @Test
  public void testCreateCscMatrix() {
    Tests.withFile(file -> {
      Npz.create(file, zout -> {
        Npz.write(zout, "format.npy", NpyCharArray.of("csc"));
      });
    });
  }

}
