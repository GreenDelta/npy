package org.openlca.npy;

import java.util.Optional;

public class NpyAsciiType implements NpyDataType {

  private final int size;

  public NpyAsciiType(int size) {
    this.size = size;
  }

  /**
   * Tries to parse the string type from the given data type description.
   *
   * @param descr a data type description like {@code |S3}
   * @return an empty optional if no ASCII string type could be recognized from
   * the description, otherwise the corresponding ASCII string type.
   */
  public static Optional<NpyAsciiType> parse(String descr) {
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
      "string_",
      "bytes_",
      "bytes",
      "Bytes",
      "S",
      "a",
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
      return Optional.of(new NpyAsciiType(0));
    try {
      int size = Integer.parseInt(s);
      return Optional.of(new NpyAsciiType(size));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  @Override
  public String symbol() {
    return "S" + size;
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
