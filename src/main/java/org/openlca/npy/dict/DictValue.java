package org.openlca.npy.dict;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A dictionary value is a sequence of key-value pairs.
 */
class DictValue {

  private final List<KeyValuePair> pairs = new ArrayList<>();

  void add(KeyValuePair pair) {
    pairs.add(pair);
  }

  KeyValuePair get(String key) {
    for (KeyValuePair pair : pairs) {
      if (Objects.equals(key, pair.key()))
        return pair;
    }
    return null;
  }

}
