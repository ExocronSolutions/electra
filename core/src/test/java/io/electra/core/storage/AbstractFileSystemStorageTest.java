package io.electra.core.storage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import io.electra.core.exception.FileSystemAccessException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
class AbstractFileSystemStorageTest {

  private static final String TEST_FILE = "test.acc";
  private AbstractFileSystemStorage storage;

  @BeforeEach
  void setUp() throws FileSystemAccessException {
    storage = new AbstractFileSystemStorage(Paths.get(TEST_FILE)) {
      @Override
      protected void doClear() {

      }

      @Override
      protected void doClose() {

      }
    };
  }

  @AfterEach
  void tearDown() throws IOException {
    Files.delete(Paths.get(TEST_FILE));
  }

  @Test
  void testClear() {
    try {
      storage.clear();
    } catch (FileSystemAccessException e) {
      fail(e);
    }
  }

  @Test
  void testClose() {
    try {
      storage.close();
    } catch (IOException e) {
      fail(e);
    }
  }

  @Test
  void testGetFileSystemAccessor() {
    assertNotNull(storage.getFileSystemAccessor());
  }
}
