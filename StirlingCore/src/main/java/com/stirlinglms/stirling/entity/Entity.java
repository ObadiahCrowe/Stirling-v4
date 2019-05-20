package com.stirlinglms.stirling.entity;

import javax.annotation.concurrent.Immutable;

@Immutable
public interface Entity<T> {

    T getDto();
}
