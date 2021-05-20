package org.openlca.npy.dict;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A dictionary value is a sequence of key-value pairs.
 */
final class PyDict implements PyValue {

  private final List<Entry> entries = new ArrayList<>();

  @Override
  public boolean isDict() {
    return true;
  }

  void put(String key, PyValue value) {
    entries.add(new Entry(key, value));
  }

  PyValue get(String key) {
    for (var entry : entries) {
      if (Objects.equals(key, entry.key()))
        return entry.value;
    }
    return PyNone.get();
  }

  public int size() {
    return entries.size();
  }

  static class Entry {

    private final String key;
    private final PyValue value;

    Entry(String key, PyValue value) {
      this.key = key;
      this.value = value;
    }

    String key() {
      return key;
    }

    PyValue value() {
      return value;
    }
  }

}
