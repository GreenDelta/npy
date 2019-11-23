package jnpz;

import java.io.File;

public class NPZ {

    public static void load(String path) {
        File file = new File(path);
        if (!file.exists() | !file.isFile()) {
            throw new IllegalArgumentException(
                    path + " is not an existing file");
        }

    }
}
