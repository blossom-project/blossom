package fr.mgargadennec.blossom.crypto.token;

import fr.mgargadennec.blossom.crypto.CryptoContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {CryptoContext.class})
public class SecretTokenServiceTest {

    @Autowired
    private TokenServiceFactory tokenServiceFactory;

    private SecretTokenService secretTokenService;

    @PostConstruct
    public void init() {
        this.secretTokenService = new SecretTokenService(tokenServiceFactory.generateSecret());
    }

    @Test(expected = Exception.class)
    public void doesNotAcceptNullSecret() {
        new SecretTokenService(null);
    }

    @Test
    public void crypt() throws Exception {
        String data = tokenServiceFactory.generateSecret();

        String token = secretTokenService.crypt(data);
        assertNotNull(token);
        assertNotEquals(token, data);
    }

    @Test
    public void decrypt() throws Exception {
        String data = tokenServiceFactory.generateSecret();
        String token = secretTokenService.crypt(data);

        assertEquals(data, secretTokenService.decrypt(token));
    }

}