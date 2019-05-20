package com.stirlinglms.stirling.dto.assignment;

import com.stirlinglms.stirling.dto.AssignmentUserDataDto;
import com.stirlinglms.stirling.entity.classroom.assignment.Assignment;
import com.stirlinglms.stirling.entity.classroom.assignment.AssignmentUserData;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Immutable
public final class AssignmentTeacherDto extends AssignmentBaseDto {

    private final Set<AssignmentUserDataDto> data;

    public AssignmentTeacherDto(Assignment assignment) {
        super(assignment);

        this.data = assignment.getAssignmentUsers().stream().map(AssignmentUserData::getDto).collect(Collectors.toSet());
    }
}
