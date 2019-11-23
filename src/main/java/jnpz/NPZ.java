package jnpz;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class NPZ {

    public static void load() {

    }

    public static void main(String[] args) {
        File file = new File("target/testdata/m.npy");
        try (InputStream in = new FileInputStream(file)){

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
