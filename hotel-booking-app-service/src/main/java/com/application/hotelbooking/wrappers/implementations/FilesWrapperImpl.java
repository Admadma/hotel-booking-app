package com.application.hotelbooking.wrappers.implementations;

import com.application.hotelbooking.wrappers.FilesWrapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
public class FilesWrapperImpl implements FilesWrapper {
    @Override
    public void copy(InputStream inputStream, Path destinationFile, CopyOption options) throws IOException {
        Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
    }
}
