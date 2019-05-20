package com.stirlinglms.stirling.entity.classroom;

import javax.annotation.concurrent.Immutable;

@Immutable
public enum AttendanceStatus {

    PRESENT("Present", false),
    NOT_PRESENT("Not Present", true),
    NOT_PRESENT_REASON("Not Present (Has Reason)", false),
    ROLL_NOT_MARKED("Roll Not Marked", true),
    CANCELLED_LESSON("Cancelled Lesson", false);

    private final String name;
    private final boolean notify;

    AttendanceStatus(String name, boolean notify) {
        this.name = name;
        this.notify = notify;
    }

    public String getName() {
        return this.name;
    }

    public boolean shouldNotify() {
        return this.notify;
    }
}
