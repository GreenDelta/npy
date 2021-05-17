package org.openlca.npy.dict;

import java.util.Collection;

final class PyTuple implements PyValue {

  private final PyValue[] values;

  PyTuple(Collection<? extends PyValue> values) {
    this.values = new PyValue[values.size()];
    int i = 0;
    for (var value : values) {
      this.values[i] = value;
      i++;
    }
  }

  @Override
  public boolean isTuple() {
    return true;
  }

  int size() {
    return values.length;
  }

  PyValue at(int i) {
    if (i < 0 || i >= values.length)
      throw new IndexOutOfBoundsException(i);
    return values[i];
  }

}
