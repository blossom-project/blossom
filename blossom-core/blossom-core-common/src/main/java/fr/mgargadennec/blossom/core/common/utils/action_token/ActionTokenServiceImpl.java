package fr.mgargadennec.blossom.core.common.utils.action_token;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.Token;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

public class ActionTokenServiceImpl implements ActionTokenService {
  private final static Logger LOGGER = LoggerFactory.getLogger(ActionTokenServiceImpl.class);

  private KeyBasedPersistenceTokenService keyBasedPersistenceTokenService;

  public ActionTokenServiceImpl(KeyBasedPersistenceTokenService keyBasedPersistenceTokenService) {
    this.keyBasedPersistenceTokenService = keyBasedPersistenceTokenService;
  }

  @Override
  public String generateToken(ActionToken actionToken) {
    Token token = keyBasedPersistenceTokenService.allocateToken(encryptTokenAsString(actionToken));
    return token.getKey();
  }

  @Override
  public ActionToken decryptToken(String aTokenKey) {
    Token token = keyBasedPersistenceTokenService.verifyToken(aTokenKey);
    return decryptTokenFromString(token.getExtendedInformation());
  }

  private ActionToken decryptTokenFromString(String anExtendedInformationsToken) {
    List<String> result =
      Lists.newArrayList(Splitter.on('|').trimResults().split(anExtendedInformationsToken));
    ActionToken actionToken = new ActionToken();
    actionToken.setUserId(Long.parseLong(result.get(0)));
    actionToken.setAction(result.get(1));
    actionToken.setExpirationDate(Instant.ofEpochMilli(Long.parseLong(result.get(2)))
      .atZone(ZoneId.systemDefault()).toLocalDateTime());
    actionToken.setAdditionalParameters(decryptAdditionalParameters(result.get(3)));
    actionToken.checkvalid();
    return actionToken;
  }

  private String encryptTokenAsString(ActionToken actionToken) {
    Preconditions.checkArgument(!StringUtils.isEmpty(actionToken.getUserId()),
      "The token user id can not be null");
    Preconditions.checkNotNull(actionToken.getAction(), "The token action can not be null");
    Preconditions.checkNotNull(actionToken.getExpirationDate(),
      "The token expiration date can not be null");

    Joiner joiner = Joiner.on('|');
    return joiner.join(actionToken.getUserId(), actionToken.getAction(),
      actionToken.getExpirationDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
      encryptAdditionalParameters(actionToken.getAdditionalParameters()));
  }

  @Required
  public void setKeyBasedPersistenceTokenService(
    KeyBasedPersistenceTokenService keyBasedPersistenceTokenService) {
    this.keyBasedPersistenceTokenService = keyBasedPersistenceTokenService;
  }

  private Map<String, String> decryptAdditionalParameters(String encryptedParams) {
    if (StringUtils.isEmpty(encryptedParams)) {
      return null;
    }

    Map<String, String> params = Maps.newHashMap();

    Splitter entrySplitter = Splitter.on("#");
    Splitter keyValueSplitter = Splitter.on("&=&");
    Iterable<String> mapEntries = entrySplitter.split(encryptedParams);
    for (String entry : mapEntries) {
      List<String> keyValueAsList = Lists.newArrayList(keyValueSplitter.split(entry));
      if (!(keyValueAsList.size() == 2)) {
        throw new RuntimeException("Can not decrypt token additional parameters");
      }
      params.put(keyValueAsList.get(0), keyValueAsList.get(1));
    }
    return params;
  }

  private String encryptAdditionalParameters(Map<String, String> encryptedParams) {
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
