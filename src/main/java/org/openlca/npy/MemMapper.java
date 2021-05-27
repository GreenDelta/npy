package org.openlca.npy;

import java.nio.channels.FileChannel;

class MemMapper {

  private final NpyHeader header;
  private final FileChannel channel;

  private MemMapper(NpyHeader header, FileChannel channel) {
    this.header = header;
    this.channel = channel;
  }

  static NpyArray all(NpyHeader header, FileChannel channel) {
    return new MemMapper(header, channel).map();
  }

  NpyArray map() {
    long unread = ((long) header.numberOfElements()) * header.dataType().size();
    long offset = header.dataOffset();
    return null;
  }

}
