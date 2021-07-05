# npy

* range reading
* Array2d.loadColumn; Array2d.loadRow  
* compare with openLCA npy  
* support writing
* Npz reading and writing  
* document the conversion table
* NpyCharArray => from byte array etc. use code points? if yes, of which size? 

`npy` is a small Java library for reading and writing arrays in the NumPy 
[NPY and NPZ](https://numpy.org/devdocs/reference/generated/numpy.lib.format.html) format.

__Features__

* written in pure Java 11 with no other dependencies and a friendly API

* (tries to be) fast; uses memory mapping where possible

* supports reading arrays of 8 bit, 16 bit, 32 bit, 64 bit signed and unsigned integers as well as booleans and 16 bit, 32 bit and 64 bit floating point numbers

* supports writing arrays of all primitive Java types: `boolean`, `byte`, `short`, `int`, `long`, `float`, `double`, `char` (#mapped to ...)

* supports all NPY format versions (1, 2, and 3)

* provides a simple `NpyArray` interface for all array types and easy conversions as well as utility classes like `Array2d`



__Limitations__

* only supports types listed above, no tuples, structs, etc.

* needs more tests

* as Java does not have unsigned types, reading 64 bit unsigned integers is only supported for values < #...



__Alternatives__

* If a Kotlin library is fine for you, checkout [npy from JetBrains-Research](https://github.com/JetBrains-Research/npy)

* Scala? maybe [this](https://github.com/priimak/scala-data) or [this](https://github.com/indix/ml2npy)



__Examples__

Reading and writing an NPY file:

```java
var array = Npy.load(file);
Npy.save(otherFile, array);
```

Type checks and type conversions:

```java
if (array.isDoubleArray()) {
  var doubles = array.asDoubleArray();
  for (double value : doubles.values()) {
    // ...    
  }
  var ints = doubles.asIntArray();  // may result in data loss
  for (int values : ints.values()) {
    // ...
  }
  var longs = ints.asLongArray();
  // ...
}
```

Reading and writing NPZ files:

```java
for (var entry : Npz.entriesOf(npzFile)) {
  var array = Npz.load(npzFile, entry);
  // ...
}
Npz.create(npzFile)
  .add("values.npy", values)
  .add("row_indices.npy", indices)
  .add("column_pointers.npy", pointers)
  .close();
```

Checked format and IO exceptions are thrown but you can opt-out by using the `unchecked` API:

```java
var array = Npy.loadUnchecked(file);
Npy.saveUnchecked(otherFile, array);
Npz.createUnchecked(npzFile)
  // ...
  .closeUnchecked();
```

Reading parts of a file; e.g. the first 100 elements:

```java
var header = NpyHeader.read(file);
var from = 0;
var count = Math.min(100, header.numberOfElements());
var array = Npy.loadRange(file, header, from, count);
```

 Using `Array2d` utilities for 2-dimensional arrays (matrices):

```java
if (Array2d.isValid(array) && Array2d.columnCountOf(array) > 2) {
  var column = Array2d.getColumn(array, 2);
  // ...  
}

var array = Array2d.loadColumn(file, header, 42);
```
