package com.learning.frontier.process;

import com.learning.frontier.model.transport.NewAccountInput;
import com.learning.frontier.model.transport.NewAccountOutput;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

@Service
public class AccountProcess {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    private CacheManager cacheManager;

    public AccountProcess(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Cacheable(
            value = "account",
            key = "{#newAccountInput.application}",
            unless = "#return != null"
    )
    public NewAccountOutput createNewAccount(NewAccountInput newAccountInput) {
        return NewAccountOutput.builder()
                .application(newAccountInput.getApplication())
                .clientId(UUID.randomUUID().toString())
                .clientSecret(this.generateClientSecret())
                .build();
    }

    /**
     * Check if an account is already register in cache
     * @param String application
     * @return true if account already register else false
     */
    public boolean checkIfAccountAlredyExist(String application) {
        Cache cache = this.cacheManager.getCache("account");
        if(cache != null ) {
            return cache.get(
                    Arrays.asList(application),
                    NewAccountOutput.class
            ) != null;
        }
        return false;
    }

    public boolean checkIfClientIdIsCorrect(String application, String clientId) {
        Cache cache = this.cacheManager.getCache("account");
        if(cache != null ) {
            NewAccountOutput newAccountOutput =  cache.get(
                    Arrays.asList(application),
                    NewAccountOutput.class
                );

            return newAccountOutput.getClientId().equals(clientId);
        }
        return false;
    }

    public boolean checkIfClientSecretIsCorrect(String application, String clientSecret) {
        Cache cache = this.cacheManager.getCache("account");
        if(cache != null ) {
            NewAccountOutput newAccountOutput =  cache.get(
                    Arrays.asList(application),
                    NewAccountOutput.class
            );

            return newAccountOutput.getClientSecret().equals(clientSecret);
        }
        return false;
    }

    public String generateClientSecret() {
        byte[] randomByte = new byte[24];
        this.secureRandom.nextBytes(randomByte);
        return this.base64Encoder.encodeToString(randomByte);
    }
}
