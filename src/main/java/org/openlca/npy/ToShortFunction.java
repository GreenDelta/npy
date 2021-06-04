package org.openlca.npy;

@FunctionalInterface
interface ToShortFunction<T> {

  short applyAsShort(T value);

}
