package com.blossomproject.autoconfigure.ui;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "blossom.security.bo.login")
public class LoginProperties {
    private Integer maxAttemps;
    private Integer hoursOfInactivity;

    public Integer getMaxAttemps() {
        return maxAttemps;
    }

    public void setMaxAttemps(Integer maxAttemps) {
        this.maxAttemps = maxAttemps;
    }

    public Integer getHoursOfInactivity() {
        return hoursOfInactivity;
    }

    public void setHoursOfInactivity(Integer hoursOfInactivity) {
        this.hoursOfInactivity = hoursOfInactivity;
    }
}
