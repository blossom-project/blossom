package fr.mgargadennec.blossom.core.common.support.event;

import org.springframework.context.ApplicationEvent;

public class BlossomUserTokenInvalidationEvent extends ApplicationEvent {

  private static final long serialVersionUID = -4943790758757922005L;

  private String username;

  public BlossomUserTokenInvalidationEvent(Object source, String username) {
    super(source);
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

}
