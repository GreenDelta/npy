# npy
`npy` is a Java 11+ library for reading and writing arrays in the NumPy 
[NPY and NPZ](https://numpy.org/devdocs/reference/generated/numpy.lib.format.html)
format.

__Features__

* written in pure Java 11 with no other dependencies
* supports reading arrays of 8-, 16-, 32-, and 64-bit signed and unsigned
  integers as well as booleans and 16-, 32-, and 64-bit floating point numbers
* supports writing arrays of all primitive Java types:
  `boolean`, `byte`, `short`, `int`, `long`, `float`, `double`, `char`
* supports all NPY format versions (1, 2, and 3)
* provides a simple `NpyArray` interface for all array types and conversions as
  well as a utility class `Array2d` for reading matrix data

__Limitations__

* only supports types listed above, no tuples, structs, etc.
* needs more tests, version is `0.0.1`

__Alternatives__
* If a Kotlin library is fine for you, checkout
  [npy from JetBrains-Research](https://github.com/JetBrains-Research/npy)
* Scala? maybe [this](https://github.com/priimak/scala-data)


## Installation
The latest version is available on the 
[Maven Central](https://search.maven.org/artifact/org.openlca/npy). If you are
using Maven add the following dependency to your `pom.xml`:

```xml
<dependency>
  <groupId>org.openlca</groupId>
  <artifactId>npy</artifactId>
  <version>0.0.1</version>
</dependency>
```

For Gradle, add this to your `build.gradle`:

```groovy
dependencies {
  compile 'org.openlca:npy:0.0.1'
}
```


## Examples

Reading and writing an NPY file:

```java
var array = Npy.read(file);
Npy.write(otherFile, array);
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
for (var entry : Npz.entries(npzFile)) {
  var array = Npz.load(npzFile, entry);
  // ...
}
Npz.create(npzFile, out -> {
  Npz.write(out, "shape.npy", shape);	
  Npz.write(out, "values.npy", values);
  // ...
});
```


Reading parts of a file; e.g. the first 100 elements:

```java
var range = Npy.readRange(file, numberOfElements, offset);
```

Using the `Array2d` utilities for 2-dimensional arrays (matrices):

```java
Npy.use(file, (chan, head) -> {
  var row42 = Array2d.readRow(chan, head, 142);
  var col42 = Array2d.readColumn(chan, head, 42);
  // ...
});
```
