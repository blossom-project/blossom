package fr.blossom.core.common.utils.action_token;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.token.TokenService;
import org.springframework.util.StringUtils;

public class ActionTokenServiceImpl implements ActionTokenService {

  private final static Logger LOGGER = LoggerFactory.getLogger(ActionTokenServiceImpl.class);

  private TokenService tokenService;

  public ActionTokenServiceImpl(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  public String generateToken(ActionToken actionToken) {
    Token token = tokenService.allocateToken(encryptTokenAsString(actionToken));
    return token.getKey();
  }

  @Override
  public ActionToken decryptToken(String aTokenKey) {
    Token token = tokenService.verifyToken(aTokenKey);
    return decryptTokenFromString(token.getExtendedInformation());
  }

  @VisibleForTesting
  protected ActionToken decryptTokenFromString(String anExtendedInformationsToken) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(anExtendedInformationsToken));
    List<String> result = Splitter.on('|').trimResults().splitToList(anExtendedInformationsToken);
    Preconditions.checkState(result.size() >= 4);

    ActionToken actionToken = new ActionToken();
    actionToken.setUserId(Long.parseLong(result.get(0)));
    actionToken.setAction(result.get(1));
    actionToken.setExpirationDate(Instant.ofEpochMilli(Long.parseLong(result.get(2))).atZone(ZoneOffset.UTC).toLocalDateTime());
    actionToken.setAdditionalParameters(decryptAdditionalParameters(result.get(3)));
    return actionToken;
  }

  @VisibleForTesting
  protected String encryptTokenAsString(ActionToken actionToken) {
    Preconditions.checkArgument(!StringUtils.isEmpty(actionToken.getUserId()),
      "The token user id can not be null");
    Preconditions.checkArgument(!StringUtils.isEmpty(actionToken.getAction()),
      "The token action can not be null");
    Preconditions.checkArgument(actionToken.getExpirationDate() != null,
      "The token expiration date can not be null");

    Joiner joiner = Joiner.on('|');
    return joiner.join(actionToken.getUserId(), actionToken.getAction(),
      actionToken.getExpirationDate().toInstant(ZoneOffset.UTC).toEpochMilli(),
      encryptAdditionalParameters(actionToken.getAdditionalParameters()));
  }

  @VisibleForTesting
  protected Map<String, String> decryptAdditionalParameters(String encryptedParams) {
    if (StringUtils.isEmpty(encryptedParams)) {
      return Maps.newHashMap();
    }

    Map<String, String> params = Maps.newHashMap();

    Splitter entrySplitter = Splitter.on("#");
    Splitter keyValueSplitter = Splitter.on("&=&");
    Iterable<String> mapEntries = entrySplitter.split(encryptedParams);
    for (String entry : mapEntries) {
      List<String> keyValueAsList = keyValueSplitter.splitToList(entry);
      Preconditions.checkState(keyValueAsList.size() == 2);
      params.put(keyValueAsList.get(0), keyValueAsList.get(1));
    }
    return params;
  }

  @VisibleForTesting
  protected String encryptAdditionalParameters(Map<String, String> encryptedParams) {
    if (encryptedParams == null) {
      return "";
    }
    List<String> entriesAsStrings = Lists.newArrayList();
    Joiner entryJoiner = Joiner.on("&=&");
    for (Map.Entry<String, String> entry : encryptedParams.entrySet()) {
      entriesAsStrings.add(entryJoiner.join(entry.getKey(), entry.getValue()));
    }
    Joiner mapJoiner = Joiner.on("#");
    return mapJoiner.join(entriesAsStrings);
  }

}
