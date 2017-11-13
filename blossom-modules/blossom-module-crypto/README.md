# Crypto module

## Token Service

Create tokens from data that can later be used to authenticate clients, or
simply retrieve the data store safely.

There are two types of tokens :
- Secret tokens, in which the data can only be retrieved with the
corresponding secret
- Signed tokens, in which the data may or may not be publicly readable,
but is signed to verify its origin.

Spring's KeyBasedPersistenceTokenService implementation is the later kind: 
the original data is contained in the token, which may not always be 
desired.

# Usage

Default token service is a secret token service using Spring's 
BouncyCastleAesGcmBytesEncryptor : AES-256 AES/GCM/NoPadding encryption.

The secret is generated on bean instantiation, which means by default tokens
can't be shared between instances or survive server restarts. To provide a
persistent token, define in configuration:

```java
@Configuration
public class BlossomConfiguration {
    
    @Bean
    public TokenService getTokenService(
            TokenServiceFactory factory,
            @Value("${my.secret}") String secret) {
        return factory.secretTokenFactory(secret);
    }
    
}
```