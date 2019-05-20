package com.stirlinglms.stirling.dto;

import com.stirlinglms.stirling.entity.classroom.time.TimeSlotOccurrence;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;

@Getter
@Immutable
public final class TimeSlotOccurrenceDto {

    private final String dayOfWeek;
    private final String startsAt;
    private final String endsAt;

    public TimeSlotOccurrenceDto(TimeSlotOccurrence occurrence) {
        this.dayOfWeek = occurrence.getDayOfWeek();
        this.startsAt = occurrence.getStartsAt();
        this.endsAt = occurrence.getEndsAt();
    }
}
