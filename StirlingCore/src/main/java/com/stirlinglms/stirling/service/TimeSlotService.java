package com.stirlinglms.stirling.service;

import com.stirlinglms.stirling.entity.classroom.time.TimeSlot;
import com.stirlinglms.stirling.entity.classroom.time.TimeSlotOccurrence;
import com.stirlinglms.stirling.entity.school.School;
import com.stirlinglms.stirling.repository.TimeSlotRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TimeSlotService extends RepositoryService<TimeSlot, TimeSlotRepository> {

    TimeSlot createTimeSlot(School school, TimeSlotOccurrence... occurrences);

    TimeSlot createTimeSlot(School school, Collection<TimeSlotOccurrence> occurrences);

    TimeSlot createTimeSlot(School school, Set<TimeSlotOccurrence> occurrences);

    TimeSlot createTimeSlot(School school, List<TimeSlotOccurrence> occurrences);

    TimeSlot getOrCreate(School school, String day, String time);
}
