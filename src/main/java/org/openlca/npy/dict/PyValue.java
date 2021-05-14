package org.openlca.npy.dict;

interface PyValue {

  default boolean isError() {
    return false;
  }

  default PyError asError() {
    return (PyError) this;
  }

  default boolean isDict() {
    return false;
  }

  default PyDict asDict() {
    return (PyDict) this;
  }

  default boolean isString() {
    return false;
  }

  default PyString asString() {
    return (PyString) this;
  }

  default boolean isIdentifier() {
    return false;
  }

  default boolean isNumber() {
    return false;
  }

  default boolean isTupel() {
    return false;
  }
}
