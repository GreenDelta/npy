package org.openlca.npy.dict;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class LexerTest {

  @Test
  public void testLex() {
    var tokens = Lexer.lex(
      "{'descr': '<i4', 'fortran_order': False, 'shape': (2,), }");
    var expectedTokens = List.of(
      new Token(TokenType.OBJECT_START, "{"),
      new Token(TokenType.STRING, "descr"),
      new Token(TokenType.COLON, ":"),
      new Token(TokenType.STRING, "<i4"),
      new Token(TokenType.COMMA, ","),
      new Token(TokenType.STRING, "fortran_order"),
      new Token(TokenType.COLON, ":"),
      new Token(TokenType.IDENTIFIER, "False"),
      new Token(TokenType.COMMA, ","),
      new Token(TokenType.STRING, "shape"),
      new Token(TokenType.COLON, ":"),
      new Token(TokenType.TUPLE_START, "("),
      new Token(TokenType.NUMBER, "2"),
      new Token(TokenType.COMMA, ","),
      new Token(TokenType.TUPLE_END, ")"),
      new Token(TokenType.COMMA, ","),
      new Token(TokenType.OBJECT_END, "}"),
      new Token(TokenType.EOF, "_")
    );
    for (int i = 0; i < tokens.size(); i++) {
      var token = tokens.get(i);
      var expected = expectedTokens.get(i);
      assertEquals(
        "invalid token at " + i + ": "
        + expected + "!=" + token, token.type, expected.type);
      if (expected.value.equals("_"))
        continue;
      assertEquals(token.value, expected.value);
    }
  }

}
