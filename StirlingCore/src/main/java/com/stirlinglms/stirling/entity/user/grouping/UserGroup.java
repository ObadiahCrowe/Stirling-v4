package com.stirlinglms.stirling.entity.user.grouping;

public class UserGroup {

    public static final UserGroup UNGROUPED = new UserGroup("None");

    private String name;

    @Deprecated
    private UserGroup() {}

    public UserGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
