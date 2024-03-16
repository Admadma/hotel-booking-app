package com.application.hotelbooking.validators;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InternetAddressValidatorTest {

    public static final String TEST_ADDRESS = "test_address";
    @InjectMocks
    private InternetAddressValidator internetAddressValidator;

    @Mock
    private InternetAddress internetAddress;

    @Test
    public void testInternetAddressValidatorThrowsAddressExceptionIfAddressIsInvalid() throws AddressException {
        doNothing().when(internetAddress).setAddress(TEST_ADDRESS);
        doThrow(AddressException.class).when(internetAddress).validate();

        assertThrows(AddressException.class,
                () -> internetAddressValidator.validate(TEST_ADDRESS));

        verify(internetAddress).setAddress(TEST_ADDRESS);
        verify(internetAddress).validate();
    }

    @Test
    public void testInternetAddressValidatorThrowsNothingIfAddressIsValid() throws AddressException {
        doNothing().when(internetAddress).setAddress(TEST_ADDRESS);
        doNothing().when(internetAddress).validate();

        assertDoesNotThrow(() -> internetAddressValidator.validate(TEST_ADDRESS));

        verify(internetAddress).setAddress(TEST_ADDRESS);
        verify(internetAddress).validate();
    }
}
