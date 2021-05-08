package org.openlca.npy.dict;

import java.util.Optional;

class Token {

  final TokenType type;
  final String value;

  Token(TokenType type, String value) {
    this.type = type;
    this.value = value;
  }

  static Token error(String value) {
    return new Token(TokenType.ERROR, value);
  }

  static Token eof() {
    return new Token(TokenType.EOF, "EOF");
  }

  static Token string(StringBuilder value) {
    return new Token(TokenType.STRING, value.toString());
  }

  static Token identifier(StringBuilder value) {
    return new Token(TokenType.IDENTIFIER, value.toString());
  }

  static Token number(StringBuilder value) {
    return new Token(TokenType.NUMBER, value.toString());
  }

  static Optional<Token> of(char c) {
    var type = TokenType.of(c);
    return type == null
      ? Optional.empty()
      : Optional.of(new Token(type, Character.toString(c)));
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
