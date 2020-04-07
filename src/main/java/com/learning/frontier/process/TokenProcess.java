package com.learning.frontier.process;

import com.learning.frontier.model.transport.TokenOutput;
import org.apache.el.parser.Token;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;

@Service
public class TokenProcess {

    private CacheManager cacheManager;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    private static final int TOKEN_VALIDITY_SECOND = 1800;

    public TokenProcess(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public TokenOutput getToken(String clientId, String scope) {
        String token = this.generateToken();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, this.TOKEN_VALIDITY_SECOND);
        Long consentedOn = calendar.getTimeInMillis();

        TokenOutput tokenOutput =  TokenOutput.builder()
                .scope(scope)
                .duration(this.TOKEN_VALIDITY_SECOND)
                .consentedOn(consentedOn)
                .token(token)
                .build();

        this.cacheToken(clientId, scope, tokenOutput);
        return tokenOutput;
    }

    public String generateToken() {
        byte[] randomByte = new byte[256];
        this.secureRandom.nextBytes(randomByte);
        return this.base64Encoder.encodeToString(randomByte);
    }

    private void cacheToken(String clientId, String scope,  TokenOutput tokenOutput) {
        Cache cache = this.cacheManager.getCache("token");
        if(cache != null) {
            cache.evictIfPresent(Arrays.asList(clientId, scope));
            cache.put(Arrays.asList(clientId, scope), tokenOutput);
        }
    }
}
