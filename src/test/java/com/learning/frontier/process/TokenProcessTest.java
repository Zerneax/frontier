package com.learning.frontier.process;

import com.learning.frontier.model.transport.TokenOutput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TokenProcessTest {

    private TokenProcess tokenProcess;

    @BeforeEach
    public void setUp() {
        this.tokenProcess = new TokenProcess();
    }

    @Test
    public void shouldTestGenerateToken() {
        String token = this.tokenProcess.generateToken();
        Assertions.assertNotNull(token);
    }

    @Test
    public void shouldTestGetToken() {
        TokenOutput tokenOutput = this.tokenProcess.getToken("clientId");

        Assertions.assertNotNull(tokenOutput);
        Assertions.assertEquals("all", tokenOutput.getScope());
        Assertions.assertEquals(1800, tokenOutput.getDuration());
        Assertions.assertNotNull(tokenOutput.getConsentedOn());
        Assertions.assertNotNull(tokenOutput.getToken());

    }
}
