package fr.mgargadennec.blossom.core.common.utils.action_token;

import java.time.LocalDateTime;
import java.util.Map;

public class ActionToken {
  private Long userId;
  private String action;
  private LocalDateTime expirationDate;
  private boolean isValid;
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
    return isValid;
  }

  public void checkvalid() {
    this.isValid = expirationDate.isAfter(LocalDateTime.now());
  }

  public Map<String, String> getAdditionalParameters() {
    return additionalParameters;
  }

  public void setAdditionalParameters(Map<String, String> additionalParameters) {
    this.additionalParameters = additionalParameters;
  }

  @Override
  public String toString() {
    return "ActionToken{" + "userId='" + userId + '\'' + ", action=" + action + ", expirationDate="
      + expirationDate + ", isValid=" + isValid + ", additionnalParameters="
      + additionalParameters + '}';
  }
}
