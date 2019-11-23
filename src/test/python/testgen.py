import os
import struct

import numpy as np

TESTDIR = "../../../target/testdata"

def write_u8bin():
    """Writes the value 255 as unsigned byte to the file u8.bin."""
    with open(TESTDIR + "/u8.bin", "wb") as f:
        f.write(struct.pack("=B", 255))


if __name__ == "__main__":
    testdir = "../../../target/testdata"
    if not os.path.exists(testdir):
        os.makedirs(testdir)

    write_u8bin()

    m = np.array([
        [1.0, 2.0, 3.0],
        [4.0, 5.0, 6.0]
    ])
    np.save(testdir + "/m.npy", m)
