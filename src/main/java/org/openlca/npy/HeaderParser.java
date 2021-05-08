package org.openlca.npy;

import java.util.Objects;

class HeaderParser {

  private final char EOF = Character.MIN_VALUE;

  private final String input;
  private int pos = -1;

  private HeaderParser(String input) {
    this.input = Objects.requireNonNull(input);
  }

  private void parse() {

    char start = nextNonEmpty();
    if (start == EOF)
      throw HeaderFormatException.isEmpty(input);
    if (start != '{')
      throw HeaderFormatException.unexpectedCharAt(input, pos, start);

    boolean fortranOrder = false;
    int[] shape = null;

    // read the key value pairs
    while (true) {

      // move to the next key
      char c = nextNonEmpty();
      if (c == '}')
        break;
      if (c == EOF)
        throwEof();
      if (!isQuote(c))
        throw HeaderFormatException.unexpectedCharAt(input, pos, c);

      // read key and colon
      var key = eatString(c);
      char colon = nextNonEmpty();
      if (colon == EOF)
        throwEof();
      if (colon != ':')
        throw HeaderFormatException.unexpectedCharAt(input, pos, colon);

      // move to the value
      char valStart = nextNonEmpty();
      if (valStart == EOF)
        throwEof();

      switch (key) {
        case "fortran_order":
          fortranOrder = eatBoolean(valStart);
          break;
        case "shape":
          //shape = eatShape(valStart);
          break;
      }


      char cc = nextNonEmpty();
      if (cc == '}')
        break;
      if (cc == ',')
        continue;
      if (cc == EOF)
        throwEof();

    }
  }

  /**
   * Moves the cursor to the position of the next non-whitespace character and
   * returns the character at that position.
   */
  private char nextNonEmpty() {
    while (true) {
      char nextChar = next();
      if (Character.isWhitespace(nextChar))
        continue;
      return nextChar;
    }
  }

  /**
   * Moves the cursor to the position of the next character and returns the
   * character at that position. Returns EOF if there is no character anymore.
   */
  private char next() {
    int nextPos = pos + 1;
    if (nextPos >= input.length())
      return EOF;
    pos = nextPos;
    return input.charAt(nextPos);
  }

  private String eatString(char firstQuote) {
    var buffer = new StringBuilder();
    while (true) {
      char c = next();
      if (c == firstQuote)
        break;
      if (c == EOF)
        throwEof();
      buffer.append(c);
    }
    return buffer.toString();
  }

  private boolean eatBoolean(char firstChar) {
    var buffer = new StringBuilder();
    buffer.append(firstChar);
    while (true) {
      char c = peek();
      if (Character.isWhitespace(c) || c == ',')
        break;
      if (c == EOF)
        throwEof();
      pos++;
      buffer.append(c);
    }
    var s = buffer.toString().toLowerCase();
    switch (s) {
      case "true":
      case "1":
        return true;
      case "false":
      case "0":
        return false;
      default:
        throw new HeaderFormatException(
          input, "invalid Boolean", pos - s.length());
    }
  }

  private char peek() {
    int nextPos = pos + 1;
    return nextPos >= input.length()
      ? EOF
      : input.charAt(nextPos);
  }

  private boolean isQuote(char c) {
    return c == '"' || c == '\'';
  }

  private void throwEof() {
    throw HeaderFormatException.unexpectedEofAt(input, pos);
  }

}
