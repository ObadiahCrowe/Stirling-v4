package com.stirlinglms.stirling.exception;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class EntityUpdateException extends Exception {

    public EntityUpdateException(String message) {
        super(message);
    }
}
