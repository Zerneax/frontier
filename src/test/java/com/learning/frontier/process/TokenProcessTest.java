package com.learning.frontier.process;

import com.learning.frontier.model.transport.TokenOutput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cache.CacheManager;

public class TokenProcessTest {

    private TokenProcess tokenProcess;
    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() {
        this.cacheManager = Mockito.mock(CacheManager.class);
        this.tokenProcess = new TokenProcess(cacheManager);
    }

    @Test
    public void shouldTestGenerateToken() {
        String token = this.tokenProcess.generateToken();
        Assertions.assertNotNull(token);
    }

    @Test
    public void shouldTestGetToken() {
        TokenOutput tokenOutput = this.tokenProcess.getToken("clientId", "scope");

        Assertions.assertNotNull(tokenOutput);
        Assertions.assertEquals("scope", tokenOutput.getScope());
        Assertions.assertEquals(1800, tokenOutput.getDuration());
        Assertions.assertNotNull(tokenOutput.getConsentedOn());
        Assertions.assertNotNull(tokenOutput.getToken());

    }
}
