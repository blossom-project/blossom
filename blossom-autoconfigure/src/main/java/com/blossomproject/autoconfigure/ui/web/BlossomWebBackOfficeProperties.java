package com.blossomproject.autoconfigure.ui.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("blossom.security.bo")
public class BlossomWebBackOfficeProperties {

  private int maxSessionsPerUser = 10;
  private int maxInactiveIntervalSeconds = 1800;

  public int getMaxSessionsPerUser() {
    return maxSessionsPerUser;
  }

  public void setMaxSessionsPerUser(int maxSessionsPerUser) {
    this.maxSessionsPerUser = maxSessionsPerUser;
  }

  public int getMaxInactiveIntervalSeconds() {
    return maxInactiveIntervalSeconds;
  }

  public void setMaxInactiveIntervalSeconds(int maxInactiveIntervalSeconds) {
    this.maxInactiveIntervalSeconds = maxInactiveIntervalSeconds;
  }
}
