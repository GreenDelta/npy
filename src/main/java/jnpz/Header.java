package jnpz;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.IllegalFormatException;
import java.util.function.Consumer;

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

    private static void parse(String s) {
        Object top = null;
        Object key = null;
        Object val = null;
        for (char c : s.toCharArray()) {
            if (c == '{') {

            }
        }
    }

    private static String parseString(Ref ref) {
        parseChar(ref, '\'');
        int i = ref.s.indexOf('\'');
        if (i < 0) {
            throw new IllegalStateException(
                    "parsing header failed: malformed string");
        }
        String s = ref.s.substring(0, i);
        ref.s = ref.s.substring(i + 1);
        return s;
    }

    private static boolean parseBoolean(Ref ref) {
        if (ref.s.substring(0, 4).equals("True")) {
            ref.s = ref.s.substring(4);
            return true;
        }
        if (ref.s.substring(0, 5).equals("False")) {
            ref.s = ref.s.substring(5);
            return false;
        }
        throw new IllegalStateException(
                "parsing header failed: excepted True or False");
    }



    private static void parseChar(Ref ref, char c) {
        char first = ref.s.charAt(0);
        if (first != c) {
            throw new IllegalStateException(
                    "parsing header failed: expected character '"
                    + c + "', found '"+ first +"'");
        }
        ref.s = ref.s.substring(1);
    }

    private static class Ref {
        String s;
    }


    public static void main(String[] args) {
        String header = "{'descr': '<i4', 'fortran_order': False, 'shape': (2,), }";
        System.out.println(parseChar(header, '{'));
        System.out.println(parseString("'descr': '<i4'")[1]);
    }

}
