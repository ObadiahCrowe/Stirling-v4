package com.stirlinglms.stirling.exception;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class NoSchoolException extends Exception {

    public NoSchoolException() {
        super("No school is present!");
    }

    public NoSchoolException(String message) {
        super(message);
    }
}
