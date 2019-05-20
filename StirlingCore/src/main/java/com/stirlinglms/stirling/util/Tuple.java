package com.stirlinglms.stirling.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;

@Getter
@Immutable
@AllArgsConstructor
public final class Tuple<A, B, C> {

    private final A type1;
    private final B type2;
    private final C type3;
}
