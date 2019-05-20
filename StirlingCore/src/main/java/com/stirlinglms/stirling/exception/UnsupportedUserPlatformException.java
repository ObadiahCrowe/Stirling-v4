package com.stirlinglms.stirling.exception;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class UnsupportedUserPlatformException extends Exception {

    public UnsupportedUserPlatformException() {
        super("You do not have the associated credentials required from access this platform!");
    }

    public UnsupportedUserPlatformException(String message) {
        super(message);
    }
}
