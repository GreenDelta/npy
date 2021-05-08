package org.openlca.npy;

public enum Endianness {

  BIG_ENDIAN('>'),

  LITTLE_ENDIAN('<'),

  HARDWARE_NATIVE('='),

  NOT_RELEVANT('|');

  private final char symbol;

  Endianness(char symbol) {
    this.symbol = symbol;
  }

  @Override
  public String toString() {
    return Character.toString(symbol);
  }
}
