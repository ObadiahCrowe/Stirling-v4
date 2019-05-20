package com.stirlinglms.stirling.entity.classroom.assignment;

import com.stirlinglms.stirling.dto.ResultDto;
import com.stirlinglms.stirling.entity.Entity;

import javax.annotation.concurrent.Immutable;

@Immutable
public class Result implements Entity<ResultDto> {

    private double maxMarks;
    private double receivedMarks;

    private Grade grade;

    private String comments;
    private double weighting;
    
    @Deprecated
    private Result() {}
    
    public Result(double maxMarks, double receivedMarks, Grade grade, String comments, double weighting) {
        this.maxMarks = maxMarks;
        this.receivedMarks = receivedMarks;
        this.grade = grade;
        this.comments = comments;
        this.weighting = weighting;
    }

    public double getMaxMarks() {
        return this.maxMarks;
    }

    public void setMaxMarks(double maxMarks) {
        this.maxMarks = maxMarks;
    }

    public double getReceivedMarks() {
        return this.receivedMarks;
    }

    public void setReceivedMarks(double receivedMarks) {
        this.receivedMarks = receivedMarks;
    }

    public Grade getGrade() {
        return this.grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public double getWeighting() {
        return this.weighting;
    }

    public void setWeighting(double weighting) {
        this.weighting = weighting;
    }

    @Override
    public ResultDto getDto() {
        return new ResultDto(this);
    }
}
