package com.stirlinglms.stirling.integration.entity;

import javax.annotation.concurrent.Immutable;

@Immutable
public class ImportableClass<I> {

    private final String name;
    private final I id;

    protected ImportableClass(String name, I id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public I getId() {
        return this.id;
    }
}
