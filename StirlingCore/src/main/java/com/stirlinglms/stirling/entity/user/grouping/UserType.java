package com.stirlinglms.stirling.entity.user.grouping;

import lombok.Getter;

import javax.annotation.concurrent.Immutable;
import java.util.stream.Stream;

@Getter
@Immutable
public enum UserType {

    VISITOR("Visitor", 0, false),
    STUDENT("Student", 1, false),
    STUDENT_LEADER("Student Leader", 2, false),
    PARENT("Parent", 3, false),
    PREFECT("Prefect", 4, false),
    TUTOR("Tutor", 5, false),
    SERVICES("Services", 6, false),
    TEACHER("Teacher", 7, true),
    GROUP_LEADER("Leader", 8, true),
    DEPUTY_PRINCIPAL("Deputy Principal", 9, true),
    PRINCIPAL("Principal", 10, true),
    ADMIN("Admin", 11, true),
    DEV("Dev", 99, true);

    private final String name;
    private final int level;
    private final boolean requires2fa;

    UserType(String name, int level, boolean requires2fa) {
        this.name = name;
        this.level = level;
        this.requires2fa = requires2fa;
    }

    public static UserType getByName(String name) {
        return Stream.of(UserType.values())
          .filter(t -> t.getName().equalsIgnoreCase(name))
          .findFirst()
          .orElse(null);
    }
}
