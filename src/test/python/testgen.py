import os
import numpy as np

if __name__ == "__main__":
    testdir = "../../../target/testdata"
    if not os.path.exists(testdir):
        os.makedirs(testdir)

    m = np.array([
        [1.0, 2.0, 3.0],
        [4.0, 5.0, 6.0]
    ])
    np.save(testdir + "/m.npy", m)
