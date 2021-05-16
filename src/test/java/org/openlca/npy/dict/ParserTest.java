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

  @Test
  public void testParseIdentifier() {
    var ids = new String[] {
      "True",
      " False",
      " class ",
    };
    for (var id : ids) {
      var value = Parser.parse(id);
      assertFalse(value.isError());
      assertTrue(value.isIdentifier());
      assertEquals(id.trim(), value.asIdentifier().value());
    }
  }

  @Test
  public void testParseInt() {
    var integers = new String[] {
      "42",
      " 42",
      " 42 ",
    };
    for (var s : integers) {
      var value = Parser.parse(s);
      assertFalse(value.isError());
      assertTrue(value.isInt());
      assertEquals(42, value.asInt().value());
    }
  }
}
