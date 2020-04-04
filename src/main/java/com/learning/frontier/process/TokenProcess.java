package com.learning.frontier.process;

import com.learning.frontier.model.transport.TokenOutput;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Calendar;

@Service
public class TokenProcess {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    private static final int TOKEN_VALIDITY_SECOND = 1800;

    public TokenProcess() {

    }

    public TokenOutput getToken(String clientId) {
        String token = this.generateToken();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, this.TOKEN_VALIDITY_SECOND);
        Long consentedOn = calendar.getTimeInMillis();

        return TokenOutput.builder()
                .scope("all")
                .duration(this.TOKEN_VALIDITY_SECOND)
                .consentedOn(consentedOn)
                .token(token)
                .build();
    }

    public String generateToken() {
        byte[] randomByte = new byte[256];
        this.secureRandom.nextBytes(randomByte);
        return this.base64Encoder.encodeToString(randomByte);
    }
}
