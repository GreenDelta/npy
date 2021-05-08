package org.openlca.npy.dict;

import java.util.ArrayList;
import java.util.List;

// https://talks.golang.org/2011/lex.slide#27
class DictLexer {

  private final char EOF = Character.MIN_VALUE;

  private final String input;
  private final List<Token> tokens = new ArrayList<>();

  private int pos = -1;

  private DictLexer(String input) {
    this.input = input;
  }

  static List<Token> lex(String input) {
    var lexer = new DictLexer(input);
    lexer.loop();
    return lexer.tokens;
  }

  void loop() {
    for (StateFunction stateFn = this::lexText; stateFn != null; ) {
      stateFn = stateFn.execute();
    }
  }

  private StateFunction lexText() {
    char c = peek();

    // skip white spaces
    while (Character.isWhitespace(c)) {
      pos++;
      c = peek();
    }

    // eof
    if (c == EOF) {
      tokens.add(new Token(TokenType.EOF, "eof"));
      return null;
    }

    // quoted string
    if (c == '"' || c == '\'')
      return this::lexString;

    // number
    if (Character.isDigit(c))
      return this::lexNumber;

    // identifiers
    if (Character.isJavaIdentifierStart(c))
      return this::lexIdentifier;

    // single character tokens
    var charType = TokenType.of(c);
    if (charType != null) {
      pos++;
      tokens.add(new Token(charType, Character.toString(c)));
      return this::lexText;
    }

    // error
    tokens.add(new Token(
      TokenType.ERROR, "unexpected character: '" + c + "'"));
    return null;
  }

  private StateFunction lexString() {
    char quote = next();
    var buffer = new StringBuilder();
    while (true) {
      char c = next();
      if (c == EOF) {
        tokens.add(new Token(TokenType.EOF, "eof"));
        return null;
      }
      if (c == quote)
        break;
      buffer.append(c);
    }
    tokens.add(new Token(TokenType.STRING, buffer.toString()));
    return this::lexText;
  }

  private StateFunction lexIdentifier() {
    var buffer = new StringBuilder();
    while (true) {
      char c = peek();
      if (!Character.isJavaIdentifierPart(c))
        break;
      pos++;
      buffer.append(c);
    }
    tokens.add(new Token(TokenType.IDENTIFIER, buffer.toString()));
    return this::lexText;
  }

  private StateFunction lexNumber() {
    var buffer = new StringBuilder();
    while (true) {
      char c = peek();
      if (!Character.isDigit(c))
        break;
      pos++;
      buffer.append(c);
    }
    tokens.add(new Token(TokenType.NUMBER, buffer.toString()));
    return this::lexText;
  }

  private char next() {
    int nextPos = pos + 1;
    if (nextPos >= input.length())
      return EOF;
    pos = nextPos;
    return input.charAt(nextPos);
  }

  private char peek() {
    int nextPos = pos + 1;
    return nextPos >= input.length()
      ? EOF
      : input.charAt(nextPos);
  }

  @FunctionalInterface
  interface StateFunction {
    StateFunction execute();
  }

  static class Token {
    final TokenType type;
    final String value;

    Token(TokenType type, String value) {
      this.type = type;
      this.value = value;
    }

    @Override
    public String toString() {
      switch (type) {
        case EOF:
          return "EOF";
        case ERROR:
          return "ERROR: " + value;
        case STRING:
          return "'" + value + "'";
        default:
          return value;
      }
    }

  }

  enum TokenType {

    IDENTIFIER,

    // quoted string
    STRING,

    // some number, integer or decimal
    NUMBER,

    COMMA,

    COLON,

    OBJECT_START,

    OBJECT_END,

    TUPLE_START,

    TUPLE_END,

    // end of file/input
    EOF,

    ERROR;

    /**
     * Returns the single-character token type for the given character. Retruns
     * null if no such token type exists.
     */
    static TokenType of(char c) {
      switch (c) {
        case ',':
          return COMMA;
        case ':':
          return COLON;
        case '{':
          return OBJECT_START;
        case '}':
          return OBJECT_END;
        case '(':
          return TUPLE_START;
        case ')':
          return TUPLE_END;
        default:
          return null;
      }
    }
  }
}
