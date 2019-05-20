package com.stirlinglms.stirling.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;

@Getter
@Immutable
@AllArgsConstructor
public final class Quadlet<A, B, C, D> {

    private final A type1;
    private final B type2;
    private final C type3;
    private final D type4;
}
