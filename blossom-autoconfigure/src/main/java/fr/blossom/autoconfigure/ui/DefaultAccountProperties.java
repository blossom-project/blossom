package fr.blossom.autoconfigure.ui;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "blossom.security.default.account")
public class DefaultAccountProperties {

  private boolean enabled;
  private String identifier;
  private String password;

  public boolean isEnabled() {
    return enabled;
  }

  public String getIdentifier() {
    return identifier;
  }

  public String getPassword() {
    return password;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
