package org.openlca.npy.dict;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class DictLexerTest {

  @Test
  public void testLex() {
    var tokens = DictLexer.lex(
      "{'descr': '<i4', 'fortran_order': False, 'shape': (2,), }");
    var expectedTokens = List.of(
      new DictLexer.Token(DictLexer.TokenType.OBJECT_START, "{"),
      new DictLexer.Token(DictLexer.TokenType.STRING, "descr"),
      new DictLexer.Token(DictLexer.TokenType.COLON, ":"),
      new DictLexer.Token(DictLexer.TokenType.STRING, "<i4"),
      new DictLexer.Token(DictLexer.TokenType.COMMA, ","),
      new DictLexer.Token(DictLexer.TokenType.STRING, "fortran_order"),
      new DictLexer.Token(DictLexer.TokenType.COLON, ":"),
      new DictLexer.Token(DictLexer.TokenType.IDENTIFIER, "False"),
      new DictLexer.Token(DictLexer.TokenType.COMMA, ","),
      new DictLexer.Token(DictLexer.TokenType.STRING, "shape"),
      new DictLexer.Token(DictLexer.TokenType.COLON, ":"),
      new DictLexer.Token(DictLexer.TokenType.TUPLE_START, "("),
      new DictLexer.Token(DictLexer.TokenType.NUMBER, "2"),
      new DictLexer.Token(DictLexer.TokenType.COMMA, ","),
      new DictLexer.Token(DictLexer.TokenType.TUPLE_END, ")"),
      new DictLexer.Token(DictLexer.TokenType.COMMA, ","),
      new DictLexer.Token(DictLexer.TokenType.OBJECT_END, "}"),
      new DictLexer.Token(DictLexer.TokenType.EOF, "_")
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
