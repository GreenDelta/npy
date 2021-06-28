package org.openlca.npy;

import java.util.Optional;

public class NpyUnicodeType implements NpyDataType {

  private final int size;

  public NpyUnicodeType(int size) {
    this.size = size;
  }

  /**
   * Tries to parse the string type from the given data type description.
   *
   * @param descr a data type description like {@code |U3}
   * @return an empty optional if no unicode string type could be recognized
   * from the description, otherwise the corresponding unicode string type.
   */
  public static Optional<NpyUnicodeType> parse(String descr) {
    if (descr == null)
      return Optional.empty();

    var s = descr.trim();
    if (s.length() == 0)
      return Optional.empty();

    // strip a possible byte order symbol
    char first = s.charAt(0);
    if (first == '='
      || first == '<'
      || first == '>'
      || first == '|') {
      s = s.substring(1);
    }

    // find a matching symbol
    var symbols = new String[]{
      "Str",
      "str",
      "str_",
      "unicode",
      "unicode_",
      "U"
    };
    boolean found = false;
    for (var symbol : symbols) {
      if (s.startsWith(symbol)) {
        s = s.substring(symbol.length());
        found = true;
        break;
      }
    }
    if (!found)
      return Optional.empty();

    // read the string length
    if (s.length() == 0)
      return Optional.of(new NpyUnicodeType(0));
    try {
      int size = Integer.parseInt(s);
      return Optional.of(new NpyUnicodeType(size));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  @Override
  public String symbol() {
    return "U" + size;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public String toString() {
    return symbol();
  }

}
