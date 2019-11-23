package jnpz;

import java.io.File;

// just for testing this for now
public class Main {

    public static void main(String[] args) {
        // NPY.load("target/testdata/m.npy");
        NPZ.load("target/testdata/sparse.npz");
    }
}
