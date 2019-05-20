package com.stirlinglms.stirling.exception;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class UnsupportedPlatformException extends Exception {

    public UnsupportedPlatformException() {
        super("This platform is unsupported!");
    }

    public UnsupportedPlatformException(String message) {
        super(message);
    }
}
