package io.electra.core.data;

import io.electra.core.exception.FileSystemAccessException;
import io.electra.core.storage.AbstractFileSystemStorage;

import java.nio.file.Path;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class DataStorageImpl extends AbstractFileSystemStorage implements DataStorage {

    public DataStorageImpl(Path dataFilePath) throws FileSystemAccessException {
        super(dataFilePath);
    }

    @Override
    protected void doClear() {

    }

    @Override
    protected void doClose() {

    }
}
