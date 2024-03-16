package com.application.hotelbooking.validators;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.springframework.stereotype.Component;

@Component
public class InternetAddressValidator {

    private InternetAddress internetAddress;

    public void validate(String address) throws AddressException {
        internetAddress = new InternetAddress(address);
        internetAddress.validate();
    }
}
