package com.stirlinglms.stirling.exception;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class UserExistsException extends Exception {

    public UserExistsException(String username) {
        super("The user, " + username + ", already exists!");
    }
}
