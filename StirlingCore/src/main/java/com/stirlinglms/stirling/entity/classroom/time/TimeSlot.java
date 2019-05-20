package com.stirlinglms.stirling.entity.classroom.time;

import com.stirlinglms.stirling.Stirling;
import com.stirlinglms.stirling.dto.TimeSlotDto;
import com.stirlinglms.stirling.entity.SaveableEntity;
import com.stirlinglms.stirling.entity.school.School;
import com.stirlinglms.stirling.repository.TimeSlotRepository;
import com.stirlinglms.stirling.service.TimeSlotService;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Reference;
import org.springframework.data.annotation.Id;

import java.util.List;

@Entity(name = "timeslots")
public class TimeSlot implements SaveableEntity<TimeSlot, TimeSlotDto, TimeSlotService, TimeSlotRepository> {

    @Id
    private Long id;

    @Reference
    private School school;

    private List<TimeSlotOccurrence> occurrences;

    @Deprecated
    private TimeSlot() {}

    public TimeSlot(School school, List<TimeSlotOccurrence> occurrences) {
        this.school = school;
        this.occurrences = occurrences;
    }

    public long getId() {
        return this.id;
    }

    public School getSchool() {
        return this.school;
    }

    public List<TimeSlotOccurrence> getOccurrences() {
        return this.occurrences;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public void setOccurrences(List<TimeSlotOccurrence> occurrences) {
        this.occurrences = occurrences;
    }

    @Override
    public TimeSlotService getService() {
        return Stirling.get().getTimeSlotService();
    }

    @Override
    public TimeSlotDto getDto() {
        return new TimeSlotDto(this);
    }
}
