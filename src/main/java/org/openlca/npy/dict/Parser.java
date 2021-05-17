package org.openlca.npy.dict;

import java.util.ArrayList;
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
    var token = tokens.get(pos);
    switch (token.type) {
      case IDENTIFIER:
        return new PyIdentifier(token.value);
      case INTEGER:
        try {
          long value = Long.parseLong(token.value);
          return new PyInt(value);
        } catch (NumberFormatException e) {
          return PyError.of(
            "failed to parse integer: '"
            + token.value + "' at:" + token.position);
        }
      case STRING:
        return new PyString(token.value);
      case TUPLE_START:
        return parseTuple(pos);
      default:
        return PyError.of(
          "syntax error: unexpected token '"
          + token.value + "' at " + token.position);
    }
  }

  private PyValue parseTuple(int start) {
    if (start >= tokens.size())
      return PyError.of("syntax error: unexpected end");
    var startToken = tokens.get(start);
    if (startToken.type != TokenType.TUPLE_START)
      return PyError.of(
        "syntax error: expected tuple start at " + startToken.position);
    var values = new ArrayList<PyValue>();
    boolean first = true;
    int pos = start + 1;
    while (true) {

      //
      if (!first) {

      }
      first = false;
    }
    return new PyTuple(values);
  }

}
