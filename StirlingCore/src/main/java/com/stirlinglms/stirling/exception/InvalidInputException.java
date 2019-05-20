package com.stirlinglms.stirling.exception;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class InvalidInputException extends Exception {

    public InvalidInputException(String message) {
        super(message);
    }
}
