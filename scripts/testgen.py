import os
import struct

import numpy as np
import scipy.sparse


TESTDIR = "../target/testdata"


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
    np.save(TESTDIR + "/_p_dense_f_matrix.npy", m)


def write_sparse_npz():
    sparse_matrix = scipy.sparse.csc_matrix(
        np.array([[0.0, 0.0, 3.0], [4.0, 0.0, 0.0]]))
    scipy.sparse.save_npz(
        TESTDIR + '/sparse.npz', sparse_matrix)


def generate_npy_files():

    types = [
        ('i1', 'b'),
        ('i2', 'i2'),
        ('i4', 'i4'),
        ('i8', 'i8'),
        ('u1', 'B'),
        ('u2', 'u2'),
        ('u4', 'u4'),
        ('u8', 'u8'),
        ('f2', 'f2'),
        ('f4', 'f4'),
        ('f8', 'f8'),
    ]

    data = [
        [1, 2, 3],
        [4, 5, 6],
    ]

    for (t1, t2) in types:
        for order in ('C', 'F'):
            for endianness in ('le', 'be'):
                file_name = '/%s_%s_%s.npy' % (t1, endianness, order.lower())
                prefix = '<' if endianness == 'le' else '>'
                np.save(TESTDIR + file_name,
                        np.array(int_data, order=order, dtype=prefix+t2))

    """
    np.save(TESTDIR + '/f8_le_f.npy', np.array(
        [[1.0, 2.0, 3.0],
         [4.0, 5.0, 6.0]],
        order='F', dtype='<f8'))
    """


if __name__ == "__main__":
    if not os.path.exists(TESTDIR):
        os.makedirs(TESTDIR)
    # write_u8bin()
    # write_npy()
    # write_sparse_npz()
    generate_npy_files()
