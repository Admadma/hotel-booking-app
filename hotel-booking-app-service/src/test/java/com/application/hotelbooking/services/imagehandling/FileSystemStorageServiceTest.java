package com.application.hotelbooking.services.imagehandling;

import com.application.hotelbooking.wrappers.FilesWrapper;
import com.application.hotelbooking.wrappers.UUIDWrapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileSystemStorageServiceTest {

    private static final String EMPTY_STRING = "";
    private static final UUID TEST_UUID = UUID.fromString("2a167ea9-850c-4059-8163-6f941561c419");
    private static final String ROOT_LOCATION = "rootlocation";
    private static final Path ROOT_LOCATION_PATH = Paths.get(ROOT_LOCATION);
    private static final String RESULT_FILE_NAME = TEST_UUID + "." + "png";
    private static final Path DESTINATION_FILE = ROOT_LOCATION_PATH.resolve(RESULT_FILE_NAME).normalize().toAbsolutePath();
    private static final String MOCK_MULTIPART_FILE_NAME = "file";
    private static final String ORIGINAL_FILENAME_PNG = "test.png";
    private static final String ORIGINAL_FILENAME_TXT = "test.txt";
    private static final String CONTENT_TYPE_IMAGE_PNG = "image/png";
    private static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";
    private static final byte[] NON_EMPTY_CONTENT = "content".getBytes();
    private static final MockMultipartFile MULTIPART_FILE_IMAGE_EMPTY = new MockMultipartFile(MOCK_MULTIPART_FILE_NAME, ORIGINAL_FILENAME_PNG,
            CONTENT_TYPE_IMAGE_PNG, EMPTY_STRING.getBytes());
    private static final MockMultipartFile MULTIPART_FILE_TEXT_NOT_EMPTY = new MockMultipartFile(MOCK_MULTIPART_FILE_NAME, ORIGINAL_FILENAME_TXT,
            CONTENT_TYPE_TEXT_PLAIN, NON_EMPTY_CONTENT);
    private static final MockMultipartFile MULTIPART_FILE_IMAGE_NOT_EMPTY = new MockMultipartFile(MOCK_MULTIPART_FILE_NAME, ORIGINAL_FILENAME_PNG,
            CONTENT_TYPE_IMAGE_PNG, NON_EMPTY_CONTENT);
    private static final String ERROR_MESSAGE_EMPTY_FILE = "Failed to store empty file.";
    private static final String ERROR_MESSAGE_FILE_FORMAT = "Not an accepted file format.";

    @InjectMocks
    private FileSystemStorageService fileSystemStorageService = new FileSystemStorageService(ROOT_LOCATION);

    @Mock
    private UUIDWrapper uuidWrapper;

    @Mock
    private FilesWrapper filesWrapper;

    @Test
    public void testConstructorShouldThrowExceptionIfImagesFolderPathIsEmpty(){

        Assertions.assertThatThrownBy(() -> new FileSystemStorageService(EMPTY_STRING))
                .isInstanceOf(StorageException.class);
    }

    @Test
    public void testConstructorShouldNotThrowExceptionIfImagesFolderPathIsNotEmpty(){

        assertDoesNotThrow(() -> new FileSystemStorageService(ROOT_LOCATION));
    }

    @Test
    public void testStoreShouldThrowStorageExceptionIfFileIsEmpty(){

        Assertions.assertThatThrownBy(() -> fileSystemStorageService.store(MULTIPART_FILE_IMAGE_EMPTY))
                .isInstanceOf(StorageException.class)
                .hasMessage(ERROR_MESSAGE_EMPTY_FILE);
    }

    @Test
    public void testStoreShouldThrowStorageExceptionIfFileIsNotInAcceptedFileFormat(){

        Assertions.assertThatThrownBy(() -> fileSystemStorageService.store(MULTIPART_FILE_TEXT_NOT_EMPTY))
                .isInstanceOf(StorageException.class)
                .hasMessage(ERROR_MESSAGE_FILE_FORMAT);
    }

    @Test
    public void testStoreShouldThrowStorageExceptionIfDestinationFileWouldBeOutsideOfRootDirectory() throws IOException {
        when(uuidWrapper.getRandomUUID()).thenReturn(TEST_UUID);
        doNothing().when(filesWrapper).copy(any(InputStream.class), eq(DESTINATION_FILE), eq(StandardCopyOption.REPLACE_EXISTING));

        String result = fileSystemStorageService.store(MULTIPART_FILE_IMAGE_NOT_EMPTY);

        verify(uuidWrapper).getRandomUUID();
        verify(filesWrapper).copy(any(InputStream.class), eq(DESTINATION_FILE), eq(StandardCopyOption.REPLACE_EXISTING));
        assertEquals(result, RESULT_FILE_NAME);
    }

    @Test
    public void testStoreShouldThrowStorageExceptionIfIOExceptionOccurs() throws IOException {
        when(uuidWrapper.getRandomUUID()).thenReturn(TEST_UUID);
        doThrow(IOException.class).when(filesWrapper).copy(any(InputStream.class), eq(DESTINATION_FILE), eq(StandardCopyOption.REPLACE_EXISTING));

        Assertions.assertThatThrownBy(() -> fileSystemStorageService.store(MULTIPART_FILE_IMAGE_NOT_EMPTY))
                .isInstanceOf(StorageException.class)
                .hasMessage("Failed to store file.");

        verify(uuidWrapper).getRandomUUID();
        verify(filesWrapper).copy(any(InputStream.class), eq(DESTINATION_FILE), eq(StandardCopyOption.REPLACE_EXISTING));
    }
}
