package org.openlca.npy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class NPZ {

    public static void load(String path) {
        File file = new File(path);
        if (!file.exists() | !file.isFile()) {
            throw new IllegalArgumentException(
                    path + " is not an existing file");
        }

        try (ZipFile zip = new ZipFile(file)){
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry e = entries.nextElement();
                System.out.println(e.getName());
                try (InputStream in = zip.getInputStream(e);
                     BufferedInputStream buf = new BufferedInputStream(in)) {
                    //System.out.println(Header.read(buf));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read NPZ file", e);
        }
    }
}
