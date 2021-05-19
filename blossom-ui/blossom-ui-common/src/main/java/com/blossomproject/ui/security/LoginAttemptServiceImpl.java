package com.blossomproject.ui.security;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class LoginAttemptServiceImpl implements LoginAttemptsService {

  private final Integer maxAttempts;
  private LoadingCache<String, List<AttemptDTO>> attemptsCache;
  private final Integer hoursOfUnavalability;

  public LoginAttemptServiceImpl(int maxAttempts, int hoursOfUnavalability) {
    this.hoursOfUnavalability = hoursOfUnavalability;
    this.maxAttempts = maxAttempts;

    attemptsCache = Caffeine
      .<String, AttemptDTO>newBuilder()
      .expireAfterWrite(hoursOfUnavalability, TimeUnit.HOURS)
      .build(key -> new ArrayList<>());
  }

  @Override
  public void successfulAttempt(String identifier, String ip) {
    List<AttemptDTO> attempts = attemptsCache.get(identifier);

    if (!attempts.isEmpty() && attempts.stream().anyMatch(attemptDTO -> ip.equals(attemptDTO.getIp()))) {
      Optional<AttemptDTO> first = attempts.stream().filter(attemptDTO -> ip.equals(attemptDTO.getIp())).findFirst();
      first.ifPresent(attempts::remove);
      attemptsCache.put(identifier, attempts);
    }

    if (attemptsCache.get(identifier).isEmpty()) {
      attemptsCache.invalidate(identifier);
    }
  }

  @Override
  public void failAttempt(String identifier, String ip) {
    List<AttemptDTO> attempts = attemptsCache.get(identifier);
    if(!attempts.isEmpty()) {
      Optional<AttemptDTO> first = attempts.stream().filter(attemptDTO -> ip.equals(attemptDTO.getIp())).findFirst();
      if(first.isPresent()) {
      first.ifPresent(attemptDTO -> attemptDTO.setAttemptNumber(attemptDTO.getAttemptNumber() + 1));
      } else {
        AttemptDTO attempt = new AttemptDTO();
        attempt.setIp(ip);
        attempt.setAttemptNumber(1);
        attempt.setBlockedSince(Date.from(Instant.now()));
        attempts.add(attempt);
      }
    } else {
      AttemptDTO attempt = new AttemptDTO();
      attempt.setIp(ip);
      attempt.setAttemptNumber(1);
      attempt.setBlockedSince(Date.from(Instant.now()));
      attempts.add(attempt);
    }
    attemptsCache.put(identifier, attempts);
  }

  @Override
  public boolean isBlocked(String identifier, String ip) {
    List<AttemptDTO> attempts = attemptsCache.get(identifier);
    if(!attempts.isEmpty() && attempts.stream().anyMatch(attemptDTO -> ip.equals(attemptDTO.getIp()))){
      Optional<AttemptDTO> first = attempts.stream().filter(attemptDTO -> ip.equals(attemptDTO.getIp())).findFirst();
      if(first.isPresent()){
        return first.get().getAttemptNumber() >= maxAttempts;
      }
      return false;
    } else {
      return false;
    }
  }

  @Override
  public Map<String, List<AttemptDTO>> get() {
    ConcurrentMap<String, List<AttemptDTO>> stringListConcurrentMap = attemptsCache.asMap();
    stringListConcurrentMap.forEach((s, attemptDTOS) -> {
      attemptDTOS.forEach(attemptDTO -> {
        if(attemptDTO.getAttemptNumber() == 10){
          Instant instant = attemptDTO.getBlockedSince().toInstant().plus(hoursOfUnavalability, ChronoUnit.HOURS).minusSeconds(Instant.now().getEpochSecond());
          long hours = instant.getEpochSecond() / 3600;
          attemptDTO.setHours(hours < 10 ? "0" + hours : String.valueOf(hours));
          long minutes = (instant.getEpochSecond() % (3600)) / 60;
          attemptDTO.setMinutes(minutes < 10 ? "0" + minutes : String.valueOf(minutes));
        }
      });
    });
    return stringListConcurrentMap;
  }


  @Override
  public void clear(String identifier, String ip) {
    List<AttemptDTO> attemptDTOS = attemptsCache.get(identifier);
    if (attemptDTOS != null) {
      attemptDTOS.stream().filter(attemptDTO -> attemptDTO.getIp().equals(ip)).findFirst().ifPresent(attemptDTOS::remove);
    }
  }
}
