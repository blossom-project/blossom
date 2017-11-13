package fr.mgargadennec.blossom.crypto.token;

import org.springframework.security.crypto.encrypt.BouncyCastleAesGcmBytesEncryptor;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SecretTokenService implements TokenService {

    private final String secret;

    public SecretTokenService(String secret) {
        if (secret == null) {
            throw new IllegalArgumentException("secret can't be null");
        }

        this.secret = secret;
    }

    @Override
    public String crypt(String data) {
        return crypt(data.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String crypt(byte[] data) {
        final String salt = KeyGenerators.string().generateKey();

        BytesEncryptor encryptor = new BouncyCastleAesGcmBytesEncryptor(secret, salt);

        String encrypted = Base64.getEncoder().encodeToString(encryptor.encrypt(data));

        return salt + "." + encrypted;
    }

    @Override
    public String decrypt(String token) {
        return new String(decryptBytes(token), StandardCharsets.UTF_8);
    }

    @Override
    public byte[] decryptBytes(String token) {
        String[] parts = token.split("\\.");
        String salt = parts[0];
        String encrypted = parts[1];

        BytesEncryptor decryptor = new BouncyCastleAesGcmBytesEncryptor(secret, salt);

        return decryptor.decrypt(Base64.getDecoder().decode(encrypted));
    }
}
