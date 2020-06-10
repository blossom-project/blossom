package com.blossomproject.ui.security;

import java.util.Date;

public class AttemptDTO {
    private String ip;
    private Integer attemptNumber;
    private Date blockedSince;
    private String hours;
    private String minutes;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(Integer attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public Date getBlockedSince() {
        return blockedSince;
    }

    public void setBlockedSince(Date blockedSince) {
        this.blockedSince = blockedSince;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }
}
