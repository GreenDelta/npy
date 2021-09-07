import os
import numpy as np
import zipfile


TESTDIR = os.path.join(os.path.dirname(__file__), "../target/testdata")


def main():

    # initialize the test folder
    if not os.path.exists(TESTDIR):
        os.makedirs(TESTDIR)
    for f in os.listdir(TESTDIR):
        os.remove(TESTDIR + "/" + f)

    # numeric types
    types = [
        ('bool', '?'),
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
        [0, 1, 2],
        [3, 4, 5],
    ]

    # generate the npy files for all combinations of storage order and
    # endianness for all data types
    for (t1, t2) in types:
        for order in ('C', 'F'):
            for endianness in ('le', 'be'):
                file_name = '/%s_%s_%s.npy' % (t1, endianness, order.lower())
                prefix = '<' if endianness == 'le' else '>'
                np.save(TESTDIR + file_name,
                        np.array(data, order=order, dtype=prefix+t2))

    np.save(TESTDIR + '/S.npy', np.array('uebergross', dtype='S'))
    np.save(TESTDIR + '/U.npy', 'übergroß')

    # create the npz file with all generated npy files
    with zipfile.ZipFile(TESTDIR + "/all.npz", 'w') as z:
        for f in os.listdir(TESTDIR):
            if not f.endswith('.npy'):
                continue
            z.write(TESTDIR + '/' + f, f)


if __name__ == "__main__":
    main()
