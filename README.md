# jnpz

`jnpz` is a small library for reading and writing 
[NPY and NPZ](https://numpy.org/devdocs/reference/generated/numpy.lib.format.html)
files.

### TODO
* maybe limiting this to 2d matrices, just with 64 bit floating point numbers?

This library supports reading and writing of 2-dimensional 64 bit floating point
arrays from and to various NPY and NPZ baded formats.

* rename it to jnpy2d? 

https://docs.scipy.org/doc/numpy-1.14.2/neps/npy-format.html

### Other implementations:

* Julia: https://github.com/fhs/NPZ.jl/blob/master/src/NPZ.jl
* Kotlin: https://github.com/JetBrains-Research/npy

Handling unsigned data: https://stackoverflow.com/a/9883582
scipy data types: https://docs.scipy.org/doc/numpy-1.13.0/reference/arrays.dtypes.html
scipy sparse matrix examples: https://docs.scipy.org/doc/scipy/reference/generated/scipy.sparse.load_npz.html#scipy.sparse.load_npz
