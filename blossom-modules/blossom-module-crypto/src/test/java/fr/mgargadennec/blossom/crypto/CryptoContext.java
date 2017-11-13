package fr.mgargadennec.blossom.crypto;

import fr.mgargadennec.blossom.crypto.token.TokenService;
import fr.mgargadennec.blossom.crypto.token.TokenServiceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
@ComponentScan(basePackageClasses = TokenService.class)
public class CryptoContext {

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
}
