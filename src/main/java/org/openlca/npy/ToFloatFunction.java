package org.openlca.npy;

@FunctionalInterface
interface ToFloatFunction<T> {

  float applyAsFloat(T value);

}
