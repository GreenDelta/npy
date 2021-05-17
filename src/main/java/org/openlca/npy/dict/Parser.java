package org.openlca.npy.dict;

import java.util.ArrayList;
import java.util.List;

class Parser {

  private final List<Token> tokens;
  private int pos = -1;

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

    return new Parser(tokens).parseNext();
  }

  private PyValue parseNext() {
    var token = peek();
    switch (token.type) {
      case IDENTIFIER:
        moveNext();
        return new PyIdentifier(token.value);
      case INTEGER:
        moveNext();
        try {
          long value = Long.parseLong(token.value);
          return new PyInt(value);
        } catch (NumberFormatException e) {
          return PyError.of(
            "failed to parse integer: '"
            + token.value + "' at:" + token.position);
        }
      case STRING:
        moveNext();
        return new PyString(token.value);
      case TUPLE_START:
        return parseTuple();
      default:
        return PyError.of(
          "syntax error: unexpected token '"
          + token.value + "' at " + token.position);
    }
  }

  private PyValue parseTuple() {
    var start = next();
    if (start.type != TokenType.TUPLE_START)
      return PyError.of(
        "syntax error: expected tuple start at " + start.position);
    var values = new ArrayList<PyValue>();
    boolean head = true;

    while (true) {
      var next = peek();
      if (next.isEof())
        return PyError.of("syntax error: unexpected end of tuple");
      if (next.type == TokenType.TUPLE_END) {
        moveNext();
        break;
      }

      if (!head) {
        if (next.type != TokenType.COMMA)
          return PyError.of("syntax error: unexpected token: " + next);
        head = true;
        moveNext();
        continue;
      }

      var value = parseNext();
      if (value.isError())
        return value;
      values.add(value);
      head = false;
    }
    return new PyTuple(values);
  }

  private Token peek() {
    int nextPos = pos + 1;
    return nextPos < tokens.size()
      ? tokens.get(nextPos)
      : Token.eof(-1);
  }

  private Token next() {
    var peeked = peek();
    if (peeked.type != TokenType.EOF) {
      pos++;
    }
    return peeked;
  }

  private void moveNext() {
    pos++;
  }

}
