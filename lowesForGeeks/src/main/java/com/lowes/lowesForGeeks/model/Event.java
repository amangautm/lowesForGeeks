package com.lowes.lowesForGeeks.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer eventId;

    private EventType eventType;

    @NotBlank
    @Size(min = 0, max = 100)
    private String name;


    private boolean isRecurring;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private Member createdBy;

    @NotBlank
    private String location;

    @NotBlank
    private String startDateTime;

    @NotBlank
    private String endDateTime;

    @JsonIgnore
    private long startDateInMills;

    @JsonIgnore
    private long endDateInMills;

    private Integer creatorTeamId;

    @JsonIgnore
    private long creationTime;

    private Integer numberOfViews;

    private Integer numberOfWatchers;

    private Integer numberOfLikes;

    private Integer numberOfUnlikes;

    @ManyToMany
    private List<Member> participants;


    Integer numberOfParticipants;

    boolean recurring;

    RecurringFrequency recurFrequency;

    public List<Member> getParticipants() {
        return participants;
    }

    public long getStartDateInMills() {
        return startDateInMills;
    }

    public void setStartDateInMills(long startDateInMills) {
        this.startDateInMills = startDateInMills;
    }

    public long getEndDateInMills() {
        return endDateInMills;
    }

    public void setEndDateInMills(long endDateInMills) {
        this.endDateInMills = endDateInMills;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public Integer getCreatorTeamId() {
        return creatorTeamId;
    }

    public void setCreatorTeamId(Integer creatorTeamId) {
        this.creatorTeamId = creatorTeamId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public Member getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Member createdBy) {
        this.createdBy = createdBy;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Integer getNumberOfViews() {
        return numberOfViews;
    }

    public void setNumberOfViews(Integer numberOfViews) {
        this.numberOfViews = numberOfViews;
    }

    public Integer getNumberOfWatchers() {
        return numberOfWatchers;
    }

    public void setNumberOfWatchers(Integer numberOfWatchers) {
        this.numberOfWatchers = numberOfWatchers;
    }

    public Integer getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(Integer numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public Integer getNumberOfUnlikes() {
        return numberOfUnlikes;
    }

    public void setNumberOfUnlikes(Integer numberOfUnlikes) {
        this.numberOfUnlikes = numberOfUnlikes;
    }

    public Integer getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(Integer numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public RecurringFrequency getRecurFrequency() {
        return recurFrequency;
    }

    public void setRecurFrequency(RecurringFrequency recurFrequency) {
        this.recurFrequency = recurFrequency;
    }

}

