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

import java.security.SecureRandom;

@Configuration("BlossomCryptoAutoconfiguration")
@AutoConfigureBefore(CommonAutoConfiguration.class)
@EntityScan(basePackageClasses = TokenServiceFactory.class)
@ConditionalOnClass({TokenServiceFactory.class})
public class CryptoAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(SecureRandom.class)
  public SecureRandom secureRandom() {
    return new SecureRandom();
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
