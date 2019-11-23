package jnpz;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class Header {

    static String read(InputStream in) {
        try {
            // The first 6 bytes are a magic string: exactly \x93NUMPY.
            byte[] bytes = new byte[6];
            int n = in.read(bytes);
            if (n != 6) {
                throw new IllegalArgumentException("Not a npy file.");
            }
            String numpy = new String(bytes, 1, 5);
            if (!numpy.equals("NUMPY")) {
                throw new IllegalArgumentException("Not a npy file.");
            }

            // check the version
            int v = in.read();
            if (v < 1) {
                throw new IllegalArgumentException(
                        "Invalid npy file, a major version >= 1 is required.");
            }

            // skip the minor version
            in.read();

            // read the header length
            bytes = new byte[2];
            n = in.read(bytes);
            if (n != 2) {
                throw new IllegalArgumentException("Not a npy file.");
            }

            // convert the unsigned short
            ByteBuffer buff = ByteBuffer.wrap(bytes);
            buff.order(ByteOrder.LITTLE_ENDIAN);
            int headerLength = buff.getShort() & 0xffff;

            // read the header string; hoping everything is ASCII
            bytes = new byte[headerLength];
            in.read(bytes);
            String header = new String(bytes);
            return header;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read header", e);
        }
    }

}
