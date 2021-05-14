package org.openlca.npy.dict;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A dictionary value is a sequence of key-value pairs.
 */
final class PyDict implements PyValue {

  private final List<PyKeyValuePair> pairs = new ArrayList<>();

  @Override
  public boolean isDict() {
    return true;
  }

  void add(PyKeyValuePair pair) {
    pairs.add(pair);
  }

  PyKeyValuePair get(String key) {
    for (PyKeyValuePair pair : pairs) {
      if (Objects.equals(key, pair.key()))
        return pair;
    }
    return null;
  }

}
