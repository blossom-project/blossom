package fr.blossom.ui.security;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginAttemptServiceImpl implements LoginAttemptsService {

  private final Integer maxAttempts;
  private LoadingCache<String, Map<String, Integer>> attemptsCache;

  public LoginAttemptServiceImpl(int maxAttempts) {
    this.maxAttempts = maxAttempts;

    attemptsCache = Caffeine
      .<String, Map<String, Integer>>newBuilder()
      .expireAfterWrite(1, TimeUnit.DAYS)
      .build(key -> Maps.<String, Integer>newConcurrentMap());
  }

  @Override
  public void successfulAttempt(String identifier, String ip) {
    Map<String, Integer> attempts = attemptsCache.get(identifier);

    if (!attempts.isEmpty() && attempts.containsKey(ip)) {
      attempts.remove(ip);
      attemptsCache.put(identifier, attempts);
    }

    if (attemptsCache.get(identifier).isEmpty()) {
      attemptsCache.invalidate(identifier);
    }
  }

  @Override
  public void failAttempt(String identifier, String ip) {
    Map<String, Integer> attempts = attemptsCache.get(identifier);
    attempts.compute(ip, (s, integer) -> integer == null ? 1 : integer+1);
    attemptsCache.put(identifier, attempts);
  }

  @Override
  public boolean isBlocked(String identifier, String ip) {
    Map<String, Integer> attempts = attemptsCache.get(identifier);
    return !attempts.isEmpty() && attempts.containsKey(ip) && attempts.get(ip) >= maxAttempts;
  }

  @Override
  public Map<String, Map<String, Integer>> get() {
    return attemptsCache.asMap();
  }
}
