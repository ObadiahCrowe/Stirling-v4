package com.stirlinglms.stirling.util.response;

import javax.annotation.concurrent.Immutable;

@Immutable
public enum ResponseCode {

    SUCCESS(200),
    ERROR(500);

    private final short code;

    ResponseCode(int code) {
        this.code = (short) code;
    }

    public short getHttpCode() {
        return this.code;
    }
}
