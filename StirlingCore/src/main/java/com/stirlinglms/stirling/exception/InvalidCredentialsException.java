package com.stirlinglms.stirling.exception;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class InvalidCredentialsException extends Exception {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
