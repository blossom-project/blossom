package fr.blossom.autoconfigure.module;

import fr.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.blossom.crypto.token.TokenServiceFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.token.TokenService;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Configuration("BlossomCryptoAutoConfiguration")
@AutoConfigureBefore(CommonAutoConfiguration.class)
@EntityScan(basePackageClasses = TokenServiceFactory.class)
@ConditionalOnClass({TokenServiceFactory.class})
public class CryptoAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(SecureRandom.class)
  public SecureRandom secureRandom() {
    try {
      return SecureRandom.getInstance("SHA1PRNG");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Can't find the SHA1PRNG algorithm for generating random numbers", e);
    }
  }

  @Bean
  @ConditionalOnMissingBean(TokenServiceFactory.class)
  public TokenServiceFactory tokenServiceFactory(
    SecureRandom secureRandom) {
    return new TokenServiceFactory(secureRandom);
  }

  @Bean
  @ConditionalOnMissingBean(TokenService.class)
  public TokenService tokenService(TokenServiceFactory tokenServiceFactory) {
    return tokenServiceFactory.statelessSecretTokenService(tokenServiceFactory.generateSecret());
  }

}
