package com.blossom_project.core.common.utils.action_token;

import java.time.Instant;
import java.util.Map;

public class ActionToken {

  private Long userId;
  private String action;
  private Instant expirationDate;
  private Map<String, String> additionalParameters;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public Instant getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(Instant endOfValidityDate) {
    this.expirationDate = endOfValidityDate;
  }

  public boolean isValid() {
    return expirationDate.isAfter(Instant.now());
  }

  public Map<String, String> getAdditionalParameters() {
    return additionalParameters;
  }

  public void setAdditionalParameters(Map<String, String> additionalParameters) {
    this.additionalParameters = additionalParameters;
  }
}
