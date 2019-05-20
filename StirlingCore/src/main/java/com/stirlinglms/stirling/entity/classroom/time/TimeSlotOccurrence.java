package com.stirlinglms.stirling.entity.classroom.time;

import com.stirlinglms.stirling.dto.TimeSlotOccurrenceDto;
import com.stirlinglms.stirling.entity.Entity;

import javax.annotation.concurrent.Immutable;

@Immutable
public class TimeSlotOccurrence implements Entity<TimeSlotOccurrenceDto> {

    private String dayOfWeek;
    private String startsAt;
    private String endsAt;

    @Deprecated
    private TimeSlotOccurrence() {}

    public TimeSlotOccurrence(String dayOfWeek, String startsAt, String endsAt) {
        this.dayOfWeek = dayOfWeek;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

    public String getDayOfWeek() {
        return this.dayOfWeek;
    }

    public String getStartsAt() {
        return this.startsAt;
    }

    public String getEndsAt() {
        return this.endsAt;
    }

    @Override
    public TimeSlotOccurrenceDto getDto() {
        return new TimeSlotOccurrenceDto(this);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TimeSlotOccurrence)) {
            return false;
        }

        TimeSlotOccurrence slot = (TimeSlotOccurrence) o;

        return slot.getDayOfWeek().equalsIgnoreCase(this.dayOfWeek) &&
          slot.getStartsAt().equalsIgnoreCase(this.startsAt) &&
          slot.getEndsAt().equalsIgnoreCase(this.endsAt);
    }
}
