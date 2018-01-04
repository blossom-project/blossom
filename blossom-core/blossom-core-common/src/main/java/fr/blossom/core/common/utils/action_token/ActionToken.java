package fr.blossom.core.common.utils.action_token;

import java.time.LocalDateTime;
import java.util.Map;

public class ActionToken {

  private Long userId;
  private String action;
  private LocalDateTime expirationDate;
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

  public LocalDateTime getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(LocalDateTime endOfValidityDate) {
    this.expirationDate = endOfValidityDate;
  }

  public boolean isValid() {
    return expirationDate.isAfter(LocalDateTime.now());
  }

  public Map<String, String> getAdditionalParameters() {
    return additionalParameters;
  }

  public void setAdditionalParameters(Map<String, String> additionalParameters) {
    this.additionalParameters = additionalParameters;
  }
}
