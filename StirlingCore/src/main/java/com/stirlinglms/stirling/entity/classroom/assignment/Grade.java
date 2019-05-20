package com.stirlinglms.stirling.entity.classroom.assignment;

import javax.annotation.concurrent.Immutable;
import java.util.stream.Stream;

@Immutable
public enum Grade {

    A_PLUS("A+", 15),
    A("A", 14),
    A_MINUS("A-", 13),

    B_PLUS("B+", 12),
    B("B", 11),
    B_MINUS("B-", 10),

    C_PLUS("C+", 9),
    C("C", 8),
    C_MINUS("C-", 7),

    D_PLUS("D+", 6),
    D("D", 5),
    D_MINUS("D-", 4),

    E_PLUS("E+", 3),
    E("E", 2),
    E_MINUS("E-", 1);

    private final String grade;
    private int value;

    Grade(String grade, int value) {
        this.grade = grade;
        this.value = value;
    }

    public String getGrade() {
        return this.grade;
    }

    public int getValue() {
        return this.value;
    }

    public static Grade fromString(String grade) {
        return Stream.of(Grade.values())
          .filter(g -> g.getGrade().equalsIgnoreCase(grade))
          .findFirst()
          .orElse(Grade.C);
    }
}
