package jnpz;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NPZ {

    public static void load() {

    }

    public static void main(String[] args) {
        File file = new File("target/testdata/m.npy");
        try (InputStream in = new FileInputStream(file);
                BufferedInputStream buffer = new BufferedInputStream(in)){

            byte[] bytes = new byte[6];
            int n = buffer.read(bytes);
            if (n!= 6) {
                throw new IllegalArgumentException("Not a npy file.");
            }
            String numpy = new String(bytes, 1, 5);
            if (!numpy.equals("NUMPY")) {
                throw new IllegalArgumentException("Not a npy file.");
            }
            int v = buffer.read();
            if (v < 1) {
                throw new IllegalArgumentException(
                        "Invalid npy file, a major version >= 1 is required.");
            }

            // skip the minor version
            buffer.read();

            byte[] lenBytes = new byte[2];
            n = buffer.read(lenBytes);
            if (n != 2) {
                throw new IllegalArgumentException("Not a npy file.");
            }

            ByteBuffer flip = ByteBuffer.wrap(lenBytes);
            flip.order(ByteOrder.LITTLE_ENDIAN);
            int len = flip.getShort() & 0xffff;
            System.out.println(len);

            byte[] hbytes = new byte[len];
            buffer.read(hbytes);
            String header = new String (hbytes);
            System.out.println(header);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
