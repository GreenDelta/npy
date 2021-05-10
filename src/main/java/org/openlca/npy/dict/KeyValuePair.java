package org.openlca.npy.dict;

class KeyValuePair {

  private final String key;
  private final Value value;

  KeyValuePair(String key, Value value) {
    this.key = key;
    this.value = value;
  }

  public String key() {
    return key;
  }

  public Value value() {
    return value;
  }
}
