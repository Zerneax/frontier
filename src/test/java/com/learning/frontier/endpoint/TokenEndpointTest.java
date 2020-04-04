package com.learning.frontier.endpoint;

import com.learning.frontier.endpoints.TokenEndpoint;
import com.learning.frontier.model.error.RequestError;
import com.learning.frontier.model.transport.TokenOutput;
import com.learning.frontier.process.TokenProcess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class TokenEndpointTest {

    private TokenProcess tokenProcess;
    private TokenEndpoint tokenEndpoint;

    @BeforeEach
    public void setUp() {
        this.tokenProcess = Mockito.mock(TokenProcess.class);
        this.tokenEndpoint = new TokenEndpoint(this.tokenProcess);
    }

    @Test
    public void shouldTestGetToken_200() {
        Mockito.doReturn(new TokenOutput()).when(this.tokenProcess).getToken(Mockito.anyString());

        ResponseEntity responseEntity = this.tokenEndpoint.getToken(this.mockFormData_ok());

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void shouldTestGetToken_400_formData_invalid() {
        Mockito.doReturn(new TokenOutput()).when(this.tokenProcess).getToken(Mockito.anyString());

        ResponseEntity responseEntity = this.tokenEndpoint.getToken(null);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        RequestError requestError = (RequestError) responseEntity.getBody();
        Assertions.assertNotNull(requestError);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.toString(), requestError.getHttpStatus());
        Assertions.assertEquals("Body is required for this path",  requestError.getMessage());
    }

    @Test
    public void shouldTestGetToken_400_clientId_invalid() {
        Mockito.doReturn(new TokenOutput()).when(this.tokenProcess).getToken(Mockito.anyString());

        ResponseEntity responseEntity = this.tokenEndpoint.getToken(this.mockFormData_clientId_missing());

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        RequestError requestError = (RequestError) responseEntity.getBody();
        Assertions.assertNotNull(requestError);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.toString(), requestError.getHttpStatus());
        Assertions.assertEquals("Parameter 'clientId' is required",  requestError.getMessage());

        responseEntity = this.tokenEndpoint.getToken(this.mockFormData_clientId_moreThanOne());

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        requestError = (RequestError) responseEntity.getBody();
        Assertions.assertNotNull(requestError);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.toString(), requestError.getHttpStatus());
        Assertions.assertEquals("Parameter 'clientId' must be unique",  requestError.getMessage());
    }

    @Test
    public void shouldTestGetToken_400_clientSecret_invalid() {
        Mockito.doReturn(new TokenOutput()).when(this.tokenProcess).getToken(Mockito.anyString());

        ResponseEntity responseEntity = this.tokenEndpoint.getToken(this.mockFormData_clientSecret_missing());

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        RequestError requestError = (RequestError) responseEntity.getBody();
        Assertions.assertNotNull(requestError);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.toString(), requestError.getHttpStatus());
        Assertions.assertEquals("Parameter 'clientSecret' is required",  requestError.getMessage());

        responseEntity = this.tokenEndpoint.getToken(this.mockFormData_clientSecret_moreThanOne());

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        requestError = (RequestError) responseEntity.getBody();
        Assertions.assertNotNull(requestError);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.toString(), requestError.getHttpStatus());
        Assertions.assertEquals("Parameter 'clientSecret' must be unique",  requestError.getMessage());
    }

    @Test
    public void shouldTestGetToken_400_scope_invalid() {
        Mockito.doReturn(new TokenOutput()).when(this.tokenProcess).getToken(Mockito.anyString());

        ResponseEntity responseEntity = this.tokenEndpoint.getToken(this.mockFormData_scope_missing());

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        RequestError requestError = (RequestError) responseEntity.getBody();
        Assertions.assertNotNull(requestError);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.toString(), requestError.getHttpStatus());
        Assertions.assertEquals("Parameter 'scope' is required",  requestError.getMessage());

        responseEntity = this.tokenEndpoint.getToken(this.mockFormData_scope_moreThanOne());

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        requestError = (RequestError) responseEntity.getBody();
        Assertions.assertNotNull(requestError);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.toString(), requestError.getHttpStatus());
        Assertions.assertEquals("Parameter 'scope' must be unique",  requestError.getMessage());
    }


    private MultiValueMap<String, String> mockFormData_ok() {
        MultiValueMap<String, String>  multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("clientId", "testId");
        multiValueMap.add("clientSecret", "testSecret");
        multiValueMap.add("scope", "testScope");

        return multiValueMap;
    }

    private MultiValueMap<String, String> mockFormData_clientId_missing() {
        MultiValueMap<String, String>  multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("clientSecret", "testSecret");
        multiValueMap.add("scope", "testScope");

        return multiValueMap;
    }


    private MultiValueMap<String, String> mockFormData_clientId_moreThanOne() {
        MultiValueMap<String, String>  multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("clientId", "testId");
        multiValueMap.add("clientId", "test2Id");
        multiValueMap.add("clientSecret", "testSecret");
        multiValueMap.add("scope", "testScope");

        return multiValueMap;
    }

    private MultiValueMap<String, String> mockFormData_clientSecret_missing() {
        MultiValueMap<String, String>  multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("clientId", "testId");
        multiValueMap.add("scope", "testScope");

        return multiValueMap;
    }


    private MultiValueMap<String, String> mockFormData_clientSecret_moreThanOne() {
        MultiValueMap<String, String>  multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("clientId", "testId");
        multiValueMap.add("clientSecret", "testSecret");
        multiValueMap.add("clientSecret", "test2Secret");
        multiValueMap.add("scope", "testScope");

        return multiValueMap;
    }

    private MultiValueMap<String, String> mockFormData_scope_missing() {
        MultiValueMap<String, String>  multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("clientId", "testId");
        multiValueMap.add("clientSecret", "testSecret");

        return multiValueMap;
    }


    private MultiValueMap<String, String> mockFormData_scope_moreThanOne() {
        MultiValueMap<String, String>  multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("clientId", "testId");
        multiValueMap.add("clientSecret", "testSecret");
        multiValueMap.add("scope", "testScope");
        multiValueMap.add("scope", "test2Scope");

        return multiValueMap;
    }
}
