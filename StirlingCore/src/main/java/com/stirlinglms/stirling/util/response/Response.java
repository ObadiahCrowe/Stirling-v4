package com.stirlinglms.stirling.util.response;

import lombok.Getter;

import javax.annotation.concurrent.Immutable;

@Getter
@Immutable
public final class Response<T> {

    private final short code;
    private final String message;
    private final T response;

    public Response(short code, String message, T response) {
        this.code = code;
        this.message = message;
        this.response = response;
    }

    public Response(ResponseCode code, String message, T response) {
        this.code = code.getHttpCode();
        this.message = message;
        this.response = response;
    }
}
