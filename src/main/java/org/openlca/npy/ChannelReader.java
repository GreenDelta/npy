package org.openlca.npy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

import org.openlca.npy.arrays.NpyArray;


class ChannelReader {

  private static final int MAX_BUFFER_SIZE = 8 * 1024;

  private final ReadableByteChannel channel;
  private final NpyHeader header;

  private ChannelReader(ReadableByteChannel channel, NpyHeader header) {
    this.channel = channel;
    this.header = header;
  }

  static NpyArray<?> read(ReadableByteChannel channel, NpyHeader header)
    throws IOException, NpyFormatException {
    return new ChannelReader(channel, header).read();
  }

  private NpyArray<?> read() throws IOException, NpyFormatException{
    long totalBytes =
      (long) header.numberOfElements()
      * (long) header.dataType().size();
    int bufferSize = totalBytes > 0 && totalBytes < ((long) MAX_BUFFER_SIZE)
      ? (int) totalBytes
      : MAX_BUFFER_SIZE;

    var builder = NpyArrayBuilder.allocate(header);

    var buffer = ByteBuffer.allocate(bufferSize);
    buffer.order(header.byteOrder());
    long readBytes = 0;
    while (readBytes < totalBytes) {
      int n = channel.read(buffer);
      if (n <= 0)
        break;
      buffer.flip();
      builder.next(buffer);
      buffer.clear();
      readBytes += n;
    }
    return builder.build();
  }
}