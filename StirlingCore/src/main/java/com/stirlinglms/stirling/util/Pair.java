package com.stirlinglms.stirling.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;

@Getter
@Immutable
@AllArgsConstructor
public final class Pair<A, B> {

    private final A type1;
    private final B type2;
}
