package com.blossomproject.ui.security;

import java.util.List;
import java.util.Map;

public interface LoginAttemptsService {

  void successfulAttempt(String identifier, String ip);

  void failAttempt(String identifier, String ip);

  boolean isBlocked(String identifier, String ip);

  void clear(String identifier, String ip);

  Map<String, List<AttemptDTO>> get();

}
