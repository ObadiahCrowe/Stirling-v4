package com.stirlinglms.stirling.exception;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class EmailExistsException extends Exception {

    public EmailExistsException(String emailAddress) {
        super("The email address, " + emailAddress + ", is already in use by another user.");
    }
}
