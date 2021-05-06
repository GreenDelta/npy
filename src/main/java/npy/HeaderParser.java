package npy;

class HeaderParser {

  private final char EOF = Character.MIN_VALUE;

  private final String input;
  private int pos;

  private HeaderParser(String input) {
    this.input = input;
  }

  private void parse() {
    forceMoveTo('{');

    // read the key value pairs
    while (true) {

      // read the next key
      char c = next();
      if (c == '}')
        break;
      if (c != '\'' && c != '"')
        throw

      if (c == EOF)
        throw new IllegalArgumentException(
          "invalid header: unexpected end at pos=" + pos);



      char cc = next();
      if (cc == '}')
        break;
      if (cc == ',')
        continue;
      if (cc == EOF)
        throw new IllegalArgumentException(
          "invalid header: unexpeced end of header at pos=" + pos);
      throw new IllegalArgumentException(

      )

    }
  }

  /**
   * Moves the cursor to the next non-whitespace character.
   */
  private char next() {

  }

  /**
   * Moves the cursor to the given character skipping all whitespaces. Throws an
   * error if another non-whitespace character was found.
   */
  private void forceMoveTo(char c) {
    while (true) {
      var next = peek();
      if (next == EOF)
        throw new IllegalArgumentException(
          "invalid header: expected to find '"
          + c + "' at pos=" + pos + " in " + input);
      pos++;
      if (next == c)
        return;
      if (Character.isWhitespace(next))
        continue;
      throw new IllegalArgumentException(
        "invalid header: expected to find '"
        + c + "' but found " + next + "at pos="
        + pos + " in " + input);
    }
  }

  private char peek() {
    int nextPos = pos + 1;
    return nextPos >= input.length()
      ? EOF
      : input.charAt(nextPos);
  }


}
