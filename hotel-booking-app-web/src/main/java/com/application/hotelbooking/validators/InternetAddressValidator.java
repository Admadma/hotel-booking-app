package com.application.hotelbooking.validators;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.springframework.stereotype.Component;

@Component
public class InternetAddressValidator {

    private InternetAddress internetAddress = new InternetAddress();

    public void validate(String address) throws AddressException {
        internetAddress.setAddress(address);
        internetAddress.validate();
    }
}
