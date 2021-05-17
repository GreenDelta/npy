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
  public void testParseStrings() {
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
  public void testParseIdentifiers() {
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

  @Test
  public void testParseIntTuples() {
    var tuples = new String[] {
      "()",
      "( 42, )",
      " (42, 42) ",
      " (42, 42, 42,) ",
      "( 42,42,42,42, )",
    };
    for (int i = 0; i < tuples.length; i++) {
      var value = Parser.parse(tuples[i]);
      assertFalse(value.isError());
      assertTrue(value.isTuple());
      var tuple = value.asTuple();
      assertEquals(i, tuple.size());
      for (int j = 0;  j < i; j++) {
        var elem = tuple.at(j);
        assertFalse(elem.isError());
        assertTrue(elem.isInt());
        assertEquals(42, elem.asInt().value());
      }
    }
  }

  @Test
  public void testParseTupleErrors() {
    var tuples = new String[] {
      "(",
      "(, )",
      "(42 42)",
      "(42, 42,,)",
      "(42,,42)",
      "(42,42,",
    };
    for (var t : tuples) {
      var value = Parser.parse(t);
      assertFalse(value.isTuple());
      assertTrue(value.isError());
    }
  }

  @Test
  public void testParseMixedTuple() {
    var value = Parser.parse(
      "(42, 'some text', (1,False,), True)");
    assertTrue(value.isTuple());
    var tuple = value.asTuple();
    assertEquals(4, tuple.size());
    assertEquals(42, tuple.at(0).asInt().value());
    assertEquals("some text", tuple.at(1).asString().value());
    assertEquals("True", tuple.at(3).asIdentifier().value());

    var inner = tuple.at(2).asTuple();
    assertEquals(2, inner.size());
    assertEquals(1, inner.at(0).asInt().value());
    assertEquals("False", inner.at(1).asIdentifier().value());
  }

}
