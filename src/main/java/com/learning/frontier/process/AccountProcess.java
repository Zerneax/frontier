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
            key = "{#newAccountInput.application, #newAccountInput.scope}",
            unless = "#return != null"
    )
    public NewAccountOutput createNewAccount(NewAccountInput newAccountInput) {
        return NewAccountOutput.builder()
                .application(newAccountInput.getApplication())
                .scope(newAccountInput.getScope())
                .clientId(UUID.randomUUID().toString())
                .clientSecret(this.generateClientSecret())
                .build();
    }

    /**
     * Check if an account is already register in cache
     * @param String application, String scope
     * @return true if account already register else false
     */
    public boolean checkIfAccountAlredyExist(String application, String scope) {
        Cache cache = this.cacheManager.getCache("account");
        if(cache != null ) {
            return cache.get(
                    Arrays.asList(application, scope),
                    NewAccountOutput.class
            ) != null;
        }
        return false;
    }

    public boolean checkIfClientIdIsCorrect(String application, String scope, String clientId) {
        Cache cache = this.cacheManager.getCache("account");
        if(cache != null ) {
            NewAccountOutput newAccountOutput =  cache.get(
                    Arrays.asList(application, scope),
                    NewAccountOutput.class
                );

            return newAccountOutput.getClientId().equals(clientId);
        }
        return false;
    }

    public String generateClientSecret() {
        byte[] randomByte = new byte[24];
        this.secureRandom.nextBytes(randomByte);
        return this.base64Encoder.encodeToString(randomByte);
    }
}
