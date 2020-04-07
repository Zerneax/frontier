package com.learning.frontier.process;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.learning.frontier.model.transport.NewAccountInput;
import com.learning.frontier.model.transport.NewAccountOutput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AccountProcessTest {

    private AccountProcess accountProcess;
    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() {
        this.cacheManager = Mockito.mock(CacheManager.class);
        this.accountProcess = new AccountProcess(this.cacheManager);
    }

    @Test
    public void shouldTestCreateNewAccount() {
        NewAccountInput newAccountInput = NewAccountInput.builder()
                .application("application")
                .build();

        NewAccountOutput newAccountOutput = this.accountProcess.createNewAccount(newAccountInput);

        Assertions.assertNotNull(newAccountOutput);
        Assertions.assertEquals("application", newAccountOutput.getApplication());
        Assertions.assertNotEquals("", newAccountOutput.getClientId());
        Assertions.assertNotEquals("", newAccountOutput.getClientSecret());
    }

    @Test
    public void shouldTestCheckIfAccountAlreadyExists_noCache() {
        Mockito.doReturn(null).when(this.cacheManager).getCache(Mockito.anyString());
        NewAccountInput newAccountInput = NewAccountInput.builder()
                .application("application")
                .build();

        Assertions.assertFalse(this.accountProcess.checkIfAccountAlredyExist(newAccountInput.getApplication()));
    }

    @Test
    public void shouldTestCheckIfAccountAlreadyExists_accountDoesntExist() {
        Mockito.doReturn(this.mockCacheAccountDoesntExist()).when(this.cacheManager).getCache(Mockito.anyString());
        NewAccountInput newAccountInput = NewAccountInput.builder()
                .application("application")
                .build();

        Assertions.assertFalse(this.accountProcess.checkIfAccountAlredyExist(newAccountInput.getApplication()));
    }

    /*@Test
    public void shouldTestCheckIfAccountAlreadyExists_accountExist() {
        NewAccountInput newAccountInput = NewAccountInput.builder()
                .application("application")
                .scope("scope")
                .build();
        this.accountProcess.createNewAccount()
        Mockito.doReturn(this.mockCache()).when(this.cacheManager).getCache(Mockito.anyString());
        NewAccountInput newAccountInput = NewAccountInput.builder()
                .application("application")
                .scope("scope")
                .build();

        Assertions.assertTrue(this.accountProcess.checkIfAccountAlredyExist(newAccountInput));
    }*/

    public Cache mockCacheAccountDoesntExist() {
        com.github.benmanes.caffeine.cache.Cache caffeineCache = Caffeine.newBuilder()
                .initialCapacity(1)
                .maximumSize(1)
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .weakKeys()
                .recordStats().build();

        Cache cache = new CaffeineCache("account", caffeineCache);
        return cache;
    }

    public CaffeineCache mockCache() {
        com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache = Caffeine.newBuilder()
                .initialCapacity(1)
                .maximumSize(2)
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .weakKeys()
                .recordStats().build();

        ArrayList<String> keys = new ArrayList<>();
        keys.add("application");
        keys.add("scope");
        caffeineCache.put(keys, new NewAccountOutput());


        return new CaffeineCache("account", caffeineCache);
        /*return */
    }
}