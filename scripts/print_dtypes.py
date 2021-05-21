# This script collects the NumPy dtype symbols and prints them in a way so that
# we can copy them easily.

import numpy as np

from typing import Dict, Set

if __name__ == '__main__':

    # collect all type names and group them
    grouped: Dict[str, Set[str]] = {}
    for key in np.sctypeDict.keys():
        if not isinstance(key, str):
            continue
        dtype = np.dtype(key)
        base = dtype.descr[0][1]
        all = grouped.get(base)
        if all is None:
            all = set()
            grouped[base] = all
        all.add(key)

    # print them so that we can copy them easily
    for dt in grouped.keys():
        print('"%s",' % dt)
        types = [g for g in grouped[dt]]
        types.sort()

        line = ''
        for t in types:
            print('  "%s",' % t)
            if line != '':
                line += ', '
            line += '"%s"' % t
        print('\n  ' + line)
        print('\n')
