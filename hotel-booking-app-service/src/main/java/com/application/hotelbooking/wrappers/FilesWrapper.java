package com.application.hotelbooking.wrappers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Path;

public interface FilesWrapper {

    void copy(InputStream inputStream, Path destinationFile, CopyOption copyOption) throws IOException;
}
