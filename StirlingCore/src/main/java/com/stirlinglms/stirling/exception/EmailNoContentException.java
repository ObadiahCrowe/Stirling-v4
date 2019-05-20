package com.stirlinglms.stirling.exception;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class EmailNoContentException extends Exception {

    public EmailNoContentException(String message) {
        super(message);
    }
}
