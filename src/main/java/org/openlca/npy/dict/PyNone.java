package org.openlca.npy.dict;

class PyNone implements PyValue {

  private static final PyNone instance = new PyNone();

  private PyNone() {
  }

  static PyNone get() {
    return instance;
  }

  @Override
  public boolean isNone() {
    return true;
  }
}
