package fr.blossom.core.common.utils.action_token;

public interface ActionTokenService {

  String generateToken(ActionToken actionToken);

  ActionToken decryptToken(String token);

}
