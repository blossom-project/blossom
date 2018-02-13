package com.blossomproject.autoconfigure.core;

import com.blossomproject.core.crypto.token.StatelessSecretTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.token.TokenService;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Configuration
@AutoConfigureBefore(CommonAutoConfiguration.class)
@PropertySource("classpath:/crypto.properties")
public class CryptoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SecureRandom.class)
    public SecureRandom secureRandom() {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Can't find the SHA1PRNG algorithm for generating random numbers",
                    e);
        }
    }


    @Bean
    @ConditionalOnMissingBean(TokenService.class)
    @ConditionalOnClass({StatelessSecretTokenService.class})
    public TokenService tokenService(@Value("${blossom.crypto.secret}") String secret) {
        return new StatelessSecretTokenService(secret);
    }

}
