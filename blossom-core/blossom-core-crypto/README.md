# Crypto module

## Token Service

Create tokens from data that can later be used to authenticate clients, or
simply retrieve the data store safely.

There are two types of tokens:
- Secret tokens, in which the data can only be retrieved with the
corresponding secret.
- Signed tokens, in which the data may or may not be publicly readable,
but is signed to verify its origin.

Spring's KeyBasedPersistenceTokenService implementation is the latter kind:
the original data is publicly readable in the token, which may not always be
desired.

### Usage

StatelessSecretTokenService is a secret token service using Spring's
BouncyCastleAesGcmBytesEncryptor : AES-256 AES/GCM/NoPadding encryption at the
time of writing.

By default the secret is generated randomly on bean instantiation, which means tokens
can't be shared between instances or survive application restarts. To provide a
persistent token, define in configuration:

```ini
blossom.crypto.secret=<your secret here>
```
