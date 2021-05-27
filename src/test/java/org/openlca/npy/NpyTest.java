package org.openlca.npy;

import static org.junit.Assert.*;

import org.junit.Test;

public class NpyTest {

  @Test
  public void testMap() {
    Tests.eachNpy(testNpy -> {
      if (testNpy.dataType() != DataType.f8)
        return; // TODO: currently only f8 is supported

        var array = Npy.mapUnchecked(testNpy.file());
        assertTrue(array.isDoubleArray());
        assertTrue(Array2D.isValid(array));
    });

  }
}
