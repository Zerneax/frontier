package com.learning.frontier.process;

import com.learning.frontier.model.transport.NewAccountInput;
import com.learning.frontier.model.transport.NewAccountOutput;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Service
public class AccountProcess {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public AccountProcess() {}

    public NewAccountOutput createNewAccount(NewAccountInput newAccountInput) {
        return NewAccountOutput.builder()
                .application(newAccountInput.getApplication())
                .scope(newAccountInput.getScope())
                .clientId(UUID.randomUUID().toString())
                .clientSecret(this.generateClientSecret())
                .build();
    }

    public String generateClientSecret() {
        byte[] randomByte = new byte[24];
        this.secureRandom.nextBytes(randomByte);
        return this.base64Encoder.encodeToString(randomByte);
    }
}
