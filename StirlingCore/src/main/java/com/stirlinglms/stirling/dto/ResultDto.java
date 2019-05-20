package com.stirlinglms.stirling.dto;

import com.stirlinglms.stirling.entity.classroom.assignment.Grade;
import com.stirlinglms.stirling.entity.classroom.assignment.Result;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;

@Getter
@Immutable
public final class ResultDto {

    private final double maximumMarks;
    private final double receivedMarks;

    private final Grade grade;

    private final String comments;
    private final double weighting;
    
    public ResultDto(Result result) {
        this.maximumMarks = result.getMaxMarks();
        this.receivedMarks = result.getReceivedMarks();

        this.grade = result.getGrade();

        this.comments = result.getComments();
        this.weighting = result.getWeighting();
    }
}
