package com.example.association.pojo;

public class ApplyParticipation {
    private Integer id;

    private Integer eventId;

    private Integer participantId;

    private Integer eventAppStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Integer participantId) {
        this.participantId = participantId;
    }

    public Integer getEventAppStatus() {
        return eventAppStatus;
    }

    public void setEventAppStatus(Integer eventAppStatus) {
        this.eventAppStatus = eventAppStatus;
    }
}