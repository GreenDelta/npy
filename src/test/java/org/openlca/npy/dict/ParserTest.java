package org.openlca.npy.dict;

import java.util.List;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

  @Test
  public void testErrorOnEmpty() {
    assertTrue(Parser.parse(null).isError());
    assertTrue(Parser.parse("").isError());
    assertTrue(Parser.parse("  ").isError());
  }

  @Test
  public void testParseString() {
    var strings = new String[] {
      "some",
      " some more",
      " some more strings  "
    };
    for (var s : strings) {
      for (var quote : List.of('"', '\'')) {
        var quoted = quote + s + quote;
        var value = Parser.parse(quoted);
        assertFalse(value.isError());
        assertTrue(value.isString());
        assertEquals(s, value.asString().value());
      }
    }
  }


}
