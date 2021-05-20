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
      new Token(0, TokenType.DICT_START, "{"),
      new Token(1, TokenType.STRING, "descr"),
      new Token(-1, TokenType.COLON, ":"),
      new Token(-1, TokenType.STRING, "<i4"),
      new Token(-1, TokenType.COMMA, ","),
      new Token(-1, TokenType.STRING, "fortran_order"),
      new Token(-1, TokenType.COLON, ":"),
      new Token(-1, TokenType.IDENTIFIER, "False"),
      new Token(-1, TokenType.COMMA, ","),
      new Token(-1, TokenType.STRING, "shape"),
      new Token(-1, TokenType.COLON, ":"),
      new Token(-1, TokenType.TUPLE_START, "("),
      new Token(-1, TokenType.INTEGER, "2"),
      new Token(-1, TokenType.COMMA, ","),
      new Token(-1, TokenType.TUPLE_END, ")"),
      new Token(-1, TokenType.COMMA, ","),
      new Token(-1, TokenType.DICT_END, "}"),
      new Token(-1, TokenType.EOF, "_")
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
