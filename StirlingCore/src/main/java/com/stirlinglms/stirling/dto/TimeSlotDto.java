package com.stirlinglms.stirling.dto;

import com.stirlinglms.stirling.entity.classroom.time.TimeSlot;
import com.stirlinglms.stirling.entity.classroom.time.TimeSlotOccurrence;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Immutable
public final class TimeSlotDto {

    private final List<TimeSlotOccurrenceDto> occurrences;

    public TimeSlotDto(TimeSlot slot) {
        this.occurrences = slot.getOccurrences().stream().map(TimeSlotOccurrence::getDto).collect(Collectors.toList());
    }
}
