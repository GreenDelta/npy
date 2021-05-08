package org.openlca.npy;

class HeaderFormatException extends RuntimeException {

  HeaderFormatException(String header, String message, int pos) {
    this(header, message + "; at position " + pos);
  }

  HeaderFormatException(String header, String message) {
    super("invalid NPY header: \"" + header + "\"; " + message);
  }

  static HeaderFormatException unexpectedEofAt(String header, int pos) {
    return new HeaderFormatException(
      header, "unexpected end of header", pos);
  }

  static HeaderFormatException unexpectedCharAt(
    String header, int pos, char c) {
    return new HeaderFormatException(
      header, "unexpected character '" + c + "'", pos);
  }

  static HeaderFormatException isEmpty(String header) {
    return new HeaderFormatException(header, "is empty");
  }

  static HeaderFormatException missingField(String header, String field) {
    return new HeaderFormatException(
      header, "field '" + field + "' is missing");
  }

}
