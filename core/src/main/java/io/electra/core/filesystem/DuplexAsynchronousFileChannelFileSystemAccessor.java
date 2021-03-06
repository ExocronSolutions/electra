package io.electra.core.filesystem;

import com.google.common.util.concurrent.Futures;
import io.electra.core.exception.FileSystemAccessException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

/**
 * Wrapper for low level file access.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class DuplexAsynchronousFileChannelFileSystemAccessor implements FileSystemAccessor {

  /**
   * The path of the file we will access.
   */
  private final Path filePath;

  /**
   * Channel used to read from file.
   */
  private AsynchronousFileChannel inputOutputChannel;

  /**
   * If the file our channel is pointing to had to be created.
   */
  private boolean hadToCreateFile;

  /**
   * Create a new low level file system accessor based on asynchronous file channels.
   *
   * @param filePath The path of the file to work on.
   * @throws FileSystemAccessException If the underlying channels cannot be created properly.
   */
  public DuplexAsynchronousFileChannelFileSystemAccessor(Path filePath)
      throws FileSystemAccessException {
    this.filePath = filePath;

    try {
      initChannels();
    } catch (IOException e) {
      throw new FileSystemAccessException("Error while accessing file system on low level", e);
    }
  }

  /**
   * Initialize the low level file channels.
   */
  private void initChannels() throws IOException {
    if (!Files.exists(filePath)) {
      hadToCreateFile = true;
      Files.createFile(filePath);
    }

    inputOutputChannel = AsynchronousFileChannel
        .open(filePath, StandardOpenOption.READ, StandardOpenOption.WRITE);
  }

  @Override
  public boolean hadToCreateFile() {
    return hadToCreateFile;
  }

  @Override
  public void close() throws IOException {
    inputOutputChannel.close();
  }

  @Override
  public void clear() throws FileSystemAccessException {
    try {
      inputOutputChannel.truncate(0);
    } catch (IOException e) {
      throw new FileSystemAccessException("Error truncating channel", e);
    }
  }

  @Override
  public Future<ByteBuffer> read(long offset, int length) {
    ByteBuffer byteBuffer = ByteBuffer.allocate(length);
    Future<Integer> bytesReadFuture = inputOutputChannel.read(byteBuffer, offset);
    return Futures.lazyTransform(bytesReadFuture, bytesRead -> {
      byteBuffer.flip();
      return byteBuffer;
    });
  }

  @Override
  public Future<Integer> write(long offset, ByteBuffer content) {
    return inputOutputChannel.write(content, offset);
  }

  @Override
  public long getFileLength() throws FileSystemAccessException {
    try {
      return inputOutputChannel.size();
    } catch (IOException e) {
      throw new FileSystemAccessException("Error reading file length", e);
    }
  }
}
