package com.stirlinglms.stirling.exception;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class EmailLimitedException extends Exception {

    public EmailLimitedException() {
        super("Email provider limit has been reached. Consider purchasing more.");
    }
}
