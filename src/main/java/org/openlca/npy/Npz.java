package org.openlca.npy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Npz {

  /**
   * Returns the names of the entries of the given NPZ file.
   *
   * @param npz a NPZ file
   * @return the names of the entries of the NPZ file
   */
  public static List<String> entries(File npz) {
    try (var zip = new ZipFile(npz)) {
      return entries(zip);
    } catch (IOException e) {
      throw new RuntimeException("failed to read zip file: " + npz, e);
    }
  }

  /**
   * Returns the names of the entries of the given NPZ file.
   *
   * @param npz a NPZ file
   * @return the names of the entries of the NPZ file
   */
  public static List<String> entries(ZipFile npz) {
    var entries = new ArrayList<String>();
    var zipEntries = npz.entries();
    while (zipEntries.hasMoreElements()) {
      var e = zipEntries.nextElement();
      if (e.isDirectory())
        continue;
      entries.add(e.getName());
    }
    return entries;
  }

  /**
   * Read an array from an entry of a NPZ file.
   *
   * @param npz   the NPZ file
   * @param entry the name of the entry in which the array is stored
   * @return the NPY array of the entry
   */
  public static NpyArray<?> read(File npz, String entry) {
    try (var zip = new ZipFile(npz)) {
      return read(zip, entry);
    } catch (IOException e) {
      throw new RuntimeException("failed to read zip file: " + npz, e);
    }
  }

  /**
   * Read an array from an entry of a NPZ file.
   *
   * @param npz   the NPZ file
   * @param entry the name of the entry in which the array is stored
   * @return the NPY array of the entry
   */
  public static NpyArray<?> read(ZipFile npz, String entry) {
    var e = npz.getEntry(entry);
    try (var stream = npz.getInputStream(e);
         var channel = Channels.newChannel(stream)) {
      return Npy.read(channel);
    } catch (IOException ex) {
      throw new RuntimeException("failed to read entry " + entry, ex);
    }
  }

  /**
   * Open the given file as an NPZ file. This function is useful when you want
   * to do multiple things with a NPZ file, e.g.
   *
   * <pre>{@code
   * Npz.use(file, npz -> {
   *   for (var entry : Npz.entries(npz)) {
   *     var array = Npz.read(npz, entry);
   *     // ...
   *   }
   * });
   * }</pre>
   *
   * @param npz the NPZ file
   * @param fn  a consumer function of the opened NPZ file
   */
  public static void use(File npz, Consumer<ZipFile> fn) {
    try (var zip = new ZipFile(npz)) {
      fn.accept(zip);
    } catch (IOException e) {
      throw new RuntimeException("failed to use NPZ file " + npz, e);
    }
  }

  public static void create(File file, Consumer<ZipOutputStream> fn) {
    try (var fileOut = new FileOutputStream(file);
         var zipOut = new ZipOutputStream(fileOut)) {
      fn.accept(zipOut);
    } catch (IOException e) {
      throw new RuntimeException("failed to create NPZ file: " + file, e);
    }
  }

  public static void write(ZipOutputStream npz, String entry, NpyArray<?> array) {
    var e = new ZipEntry(entry);
    try {
      npz.putNextEntry(e);
      Npy.write(npz, array);
      npz.closeEntry();
    } catch (IOException ex) {
      throw new RuntimeException("failed to write NPZ entry: " + entry, ex);
    }
  }

}
