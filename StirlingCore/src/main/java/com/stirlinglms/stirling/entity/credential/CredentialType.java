package com.stirlinglms.stirling.entity.credential;

import javax.annotation.concurrent.Immutable;
import java.util.stream.Stream;

@Immutable
public enum CredentialType {

    DAYMAP("Daymap"),
    MOODLE("Moodle"),
    GOOGLE_CLASSROOM("Google Classroom");

    private final String name;

    CredentialType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static CredentialType getByName(String name) {
        return Stream.of(CredentialType.values())
          .filter(t -> t.getName().equalsIgnoreCase(name))
          .findFirst()
          .orElse(null);
    }
}
