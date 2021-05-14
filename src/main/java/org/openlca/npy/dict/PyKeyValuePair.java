package org.openlca.npy.dict;

class PyKeyValuePair {

  private final String key;
  private final PyValue value;

  PyKeyValuePair(String key, PyValue value) {
    this.key = key;
    this.value = value;
  }

  public String key() {
    return key;
  }

  public PyValue value() {
    return value;
  }
}
