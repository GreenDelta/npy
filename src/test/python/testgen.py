import os
import struct

import numpy as np
import scipy.sparse


TESTDIR = "../../../target/testdata"


def write_u8bin():
    """Writes the value 255 as unsigned byte to the file u8.bin."""
    with open(TESTDIR + "/u8.bin", "wb") as f:
        f.write(struct.pack("=B", 255))


def write_npy():
    m = np.array([
        [1.0, 2.0, 3.0],
        [4.0, 5.0, 6.0]
    ])
    np.save(TESTDIR + "/m.npy", m)


def write_dense_f_matrix():
    m = np.array([
        [1.0, 3.0, 5.0],
        [2.0, 4.0, 6.0]
    ], order='F')
    np.save(TESTDIR + "/_p_dense_f_matrix.npy")


def write_sparse_npz():
    sparse_matrix = scipy.sparse.csc_matrix(
        np.array([[0.0, 0.0, 3.0], [4.0, 0.0, 0.0]]))
    scipy.sparse.save_npz(
        TESTDIR + '/sparse.npz', sparse_matrix)


if __name__ == "__main__":
    if not os.path.exists(TESTDIR):
        os.makedirs(TESTDIR)
    write_u8bin()
    write_npy()
    write_sparse_npz()
