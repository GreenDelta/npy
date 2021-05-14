package org.openlca.npy.dict;

import java.util.List;

class Parser {

  private final List<Token> tokens;

  private Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  static PyValue parse(String text) {
    if (text == null)
      return PyError.of("empty input");
    var tokens = Lexer.lex(text);
    if (tokens.isEmpty())
      return PyError.of("empty input");

    // check if there is an error token
    for (var token : tokens) {
      if (token.type == TokenType.ERROR)
        return PyError.of(
          "syntax error: " + token.value + "; at " + token.position);
    }

    return new Parser(tokens).parseNext(0);
  }

  private PyValue parseNext(int pos) {
    if (pos >= tokens.size())
      return PyError.of("empty input");
    var first = tokens.get(0);
    switch (first.type) {
      case STRING:
        return new PyString(first.value);
      default:
        return PyError.of(
          "syntax error: unexpected token '"
          + first.value + "' at " + first.position);
    }
  }

}
