//package com.application.hotelbooking.services.imagehandling;
//
//import com.application.hotelbooking.wrappers.FilesWrapper;
//import com.application.hotelbooking.wrappers.UUIDWrapper;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mock.web.MockMultipartFile;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class FileSystemStorageServiceTest {
//
//    private static final String EMPTY_STRING = "";
//    private static final UUID TEST_UUID = UUID.fromString("2a167ea9-850c-4059-8163-6f941561c419");
//    private static final String ROOT_LOCATION = "rootlocation";
//    private static final Path ROOT_LOCATION_PATH = Paths.get(ROOT_LOCATION);
//    private static final String RESULT_FILE_NAME = TEST_UUID + "." + "png";
//    private static final Path DESTINATION_FILE = ROOT_LOCATION_PATH.resolve(RESULT_FILE_NAME).normalize().toAbsolutePath();
//    private static final String MOCK_MULTIPART_FILE_NAME = "file";
//    private static final String ORIGINAL_FILENAME_PNG = "test.png";
//    private static final String ORIGINAL_FILENAME_TXT = "test.txt";
//    private static final String CONTENT_TYPE_IMAGE_PNG = "image/png";
//    private static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";
//    private static final byte[] NON_EMPTY_CONTENT = "content".getBytes();
//    private static final MockMultipartFile MULTIPART_FILE_IMAGE_EMPTY = new MockMultipartFile(MOCK_MULTIPART_FILE_NAME, ORIGINAL_FILENAME_PNG,
//            CONTENT_TYPE_IMAGE_PNG, EMPTY_STRING.getBytes());
//    private static final MockMultipartFile MULTIPART_FILE_TEXT_NOT_EMPTY = new MockMultipartFile(MOCK_MULTIPART_FILE_NAME, ORIGINAL_FILENAME_TXT,
//            CONTENT_TYPE_TEXT_PLAIN, NON_EMPTY_CONTENT);
//    private static final MockMultipartFile MULTIPART_FILE_IMAGE_NOT_EMPTY = new MockMultipartFile(MOCK_MULTIPART_FILE_NAME, ORIGINAL_FILENAME_PNG,
//            CONTENT_TYPE_IMAGE_PNG, NON_EMPTY_CONTENT);
//    private static final String ERROR_MESSAGE_EMPTY_FILE = "Failed to store empty file.";
//    private static final String ERROR_MESSAGE_FILE_FORMAT = "Not an accepted file format.";
//
//    @Mock
//    private StorageProperties storageProperties;
//
//    @Mock
//    private UUIDWrapper uuidWrapper;
//
//    @Mock
//    private FilesWrapper filesWrapper;
//
//    @Test
//    public void testConstructorShouldThrowExceptionIfStoragePropertiesUploadLocationIsEmpty(){
//        when(storageProperties.getLocation()).thenReturn(EMPTY_STRING);
//
//        Assertions.assertThatThrownBy(() -> new FileSystemStorageService(storageProperties, uuidWrapper, filesWrapper))
//                .isInstanceOf(StorageException.class);
//
//        verify(storageProperties).getLocation();
//    }
//
//    @Test
//    public void testConstructorShouldNotThrowExceptionIfStoragePropertiesUploadLocationIsPresent(){
//        when(storageProperties.getLocation()).thenReturn(ROOT_LOCATION);
//
//        assertDoesNotThrow(() -> new FileSystemStorageService(storageProperties, uuidWrapper, filesWrapper));
//
//        verify(storageProperties, times(2)).getLocation();
//    }
//
//    @Test
//    public void testStoreShouldThrowStorageExceptionIfFileIsEmpty(){
//        when(storageProperties.getLocation()).thenReturn(ROOT_LOCATION);
//        FileSystemStorageService fileSystemStorageService = new FileSystemStorageService(storageProperties, uuidWrapper, filesWrapper);
//
//        Assertions.assertThatThrownBy(() -> fileSystemStorageService.store(MULTIPART_FILE_IMAGE_EMPTY))
//                .isInstanceOf(StorageException.class)
//                .hasMessage(ERROR_MESSAGE_EMPTY_FILE);
//
//        verify(storageProperties, times(2)).getLocation();
//    }
//
//    @Test
//    public void testStoreShouldThrowStorageExceptionIfFileIsNotInAcceptedFileFormat(){
//        when(storageProperties.getLocation()).thenReturn(ROOT_LOCATION);
//        FileSystemStorageService fileSystemStorageService = new FileSystemStorageService(storageProperties, uuidWrapper, filesWrapper);
//
//        Assertions.assertThatThrownBy(() -> fileSystemStorageService.store(MULTIPART_FILE_TEXT_NOT_EMPTY))
//                .isInstanceOf(StorageException.class)
//                .hasMessage(ERROR_MESSAGE_FILE_FORMAT);
//
//        verify(storageProperties, times(2)).getLocation();
//    }
//
//    @Test
//    public void testStoreShouldThrowStorageExceptionIfDestinationFileWouldBeOutsideOfRootDirectory() throws IOException {
//        when(storageProperties.getLocation()).thenReturn(ROOT_LOCATION);
//        when(uuidWrapper.getRandomUUID()).thenReturn(TEST_UUID);
//        doNothing().when(filesWrapper).copy(any(InputStream.class), eq(DESTINATION_FILE), eq(StandardCopyOption.REPLACE_EXISTING));
//        FileSystemStorageService fileSystemStorageService = new FileSystemStorageService(storageProperties, uuidWrapper, filesWrapper);
//
//        String result = fileSystemStorageService.store(MULTIPART_FILE_IMAGE_NOT_EMPTY);
//
//        verify(storageProperties, times(2)).getLocation();
//        verify(uuidWrapper).getRandomUUID();
//        verify(filesWrapper).copy(any(InputStream.class), eq(DESTINATION_FILE), eq(StandardCopyOption.REPLACE_EXISTING));
//        assertEquals(result, RESULT_FILE_NAME);
//    }
//
//    @Test
//    public void testStoreShouldThrowStorageExceptionIfIOExceptionOccurs() throws IOException {
//        when(storageProperties.getLocation()).thenReturn(ROOT_LOCATION);
//        when(uuidWrapper.getRandomUUID()).thenReturn(TEST_UUID);
//        doThrow(IOException.class).when(filesWrapper).copy(any(InputStream.class), eq(DESTINATION_FILE), eq(StandardCopyOption.REPLACE_EXISTING));
//        FileSystemStorageService fileSystemStorageService = new FileSystemStorageService(storageProperties, uuidWrapper, filesWrapper);
//
//        Assertions.assertThatThrownBy(() -> fileSystemStorageService.store(MULTIPART_FILE_IMAGE_NOT_EMPTY))
//                .isInstanceOf(StorageException.class)
//                .hasMessage("Failed to store file.");
//
//        verify(storageProperties, times(2)).getLocation();
//        verify(uuidWrapper).getRandomUUID();
//        verify(filesWrapper).copy(any(InputStream.class), eq(DESTINATION_FILE), eq(StandardCopyOption.REPLACE_EXISTING));
//    }
//}
