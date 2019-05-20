package com.stirlinglms.stirling.exception;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class EmailNoRecipientException extends Exception {

    public EmailNoRecipientException(String message) {
        super(message);
    }
}
