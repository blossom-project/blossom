package fr.mgargadennec.blossom.autoconfigure.module;

import fr.mgargadennec.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.mgargadennec.blossom.crypto.token.TokenService;
import fr.mgargadennec.blossom.crypto.token.TokenServiceFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;

import java.security.SecureRandom;

@Configuration
@ConditionalOnClass({TokenService.class})
@AutoConfigureAfter(CommonAutoConfiguration.class)
@EntityScan(basePackageClasses = TokenService.class)
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
        return tokenServiceFactory.secretTokenService(tokenServiceFactory.generateSecret());
    }

}
