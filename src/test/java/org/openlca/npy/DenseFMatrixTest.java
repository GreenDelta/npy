package org.openlca.npy;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

public class DenseFMatrixTest {

	@Test
	public void testGet() {
		DenseFMatrix m = new DenseFMatrix(2, 3, new double[]{
				1., 2., 3., 4., 5., 6.
		});
		for (int i = 0; i < m.rows; i++) {
			for (int j = 0; j < m.columns; j++) {
				double v = i + m.rows * j + 1;
				Assert.assertEquals(v, m.get(i, j), 1e-10);
			}
		}
	}

	@Test
	public void testGetColumn() {
		DenseFMatrix m = new DenseFMatrix(2, 3, new double[]{
				1., 2., 3., 4., 5., 6.
		});
		Assert.assertArrayEquals(m.getColumn(0), new double[]{1., 2.}, 1e-10);
		Assert.assertArrayEquals(m.getColumn(1), new double[]{3., 4.}, 1e-10);
		Assert.assertArrayEquals(m.getColumn(2), new double[]{5., 6.}, 1e-10);
	}

	@Test
	public void testGetRow() {
		DenseFMatrix m = new DenseFMatrix(2, 3, new double[]{
				1., 2., 3., 4., 5., 6.
		});
		Assert.assertArrayEquals(m.getRow(0), new double[]{1., 3., 5.}, 1e-10);
		Assert.assertArrayEquals(m.getRow(1), new double[]{2., 4., 6.}, 1e-10);
	}

	@Test
	public void testWriteRead() throws Exception {
		File npy = new File("target/testdata/_j_dense_f_matrix.npy");
		if (!npy.getParentFile().exists()) {
			npy.getParentFile().mkdirs();
		}
		DenseFMatrix m = new DenseFMatrix(2, 3, new double[]{
				1., 2., 3., 4., 5., 6.
		});
		m.toNPY(npy);
		try (FileInputStream fis = new FileInputStream(npy)) {
			System.out.println(Header.read(fis));
		}
	}

}
