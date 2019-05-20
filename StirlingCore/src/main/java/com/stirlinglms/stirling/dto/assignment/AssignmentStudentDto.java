package com.stirlinglms.stirling.dto.assignment;

import com.stirlinglms.stirling.dto.AssignmentUserDataDto;
import com.stirlinglms.stirling.entity.classroom.assignment.Assignment;
import com.stirlinglms.stirling.entity.classroom.assignment.AssignmentUserData;
import com.stirlinglms.stirling.entity.user.User;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;

@Getter
@Immutable
public final class AssignmentStudentDto extends AssignmentBaseDto {

    private final AssignmentUserDataDto data;

    public AssignmentStudentDto(Assignment assignment, User user) {
        super(assignment);

        AssignmentUserData data = assignment.getAssignmentUsers().stream()
          .filter(u -> u.getUser().getId().equals(user.getId()))
          .findFirst()
          .orElse(null);

        this.data = data == null ? null : data.getDto();
    }
}
