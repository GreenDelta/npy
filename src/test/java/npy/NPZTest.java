package npy;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class NPZTest {

    @Test
    public void readU8() throws Exception {
        File f = new File("target/testdata/u8.bin");
        InputStream in = new FileInputStream(f);
        int u8 = in.read();
        Assert.assertEquals(255, u8);
    }
}
