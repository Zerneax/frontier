package com.learning.frontier.endpoints;

import com.learning.frontier.model.error.RequestError;
import com.learning.frontier.model.transport.TokenOutput;
import com.learning.frontier.process.AccountProcess;
import com.learning.frontier.process.TokenProcess;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/token")
public class TokenEndpoint {

    private TokenProcess tokenProcess;
    private AccountProcess accountProcess;

    private static final String CLIENT_ID_PARAM = "clientId";
    private static final String CLIENT_SECRET_PARAM = "clientSecret";
    private static final String SCOPE_PARAM = "scope";
    private static final String APPLICATION_PARAM = "application";

    public TokenEndpoint(TokenProcess tokenProcess, AccountProcess accountProcess) {
        this.tokenProcess = tokenProcess;
        this.accountProcess = accountProcess;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity getToken(@RequestParam MultiValueMap<String, String> formData) {

        RequestError requestError = this.checkParametersGetToken(formData);
        if (requestError != null) {
            return ResponseEntity.badRequest().body(requestError);
        }

        if(!this.accountProcess.checkIfAccountAlredyExist(formData.getFirst(this.APPLICATION_PARAM))) {
            RequestError requestErrorNotFound = RequestError.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.toString())
                    .message("Application don't exist")
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(requestErrorNotFound);
        }

        String clientId = formData.getFirst(this.CLIENT_ID_PARAM);
        String scope = formData.getFirst(this.SCOPE_PARAM);

        TokenOutput tokenOutput = this.tokenProcess.getToken(clientId, scope);

        return ResponseEntity.ok(tokenOutput);
    }

    private RequestError checkParametersGetToken(MultiValueMap<String, String> formData) {
        if(formData == null) {
            return RequestError.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.toString())
                    .message("Body is required for this path")
                    .build();
        }

        if(formData.get(this.CLIENT_ID_PARAM) == null || formData.get(this.CLIENT_ID_PARAM).isEmpty()) {
            return RequestError.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.toString())
                    .message("Parameter '" + this.CLIENT_ID_PARAM + "' is required")
                    .build();
        }

        if(formData.get(this.CLIENT_SECRET_PARAM) == null || formData.get(this.CLIENT_SECRET_PARAM).isEmpty()) {
            return RequestError.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.toString())
                    .message("Parameter '" + this.CLIENT_SECRET_PARAM + "' is required")
                    .build();
        }

        if(formData.get(this.SCOPE_PARAM) == null || formData.get(this.SCOPE_PARAM).isEmpty()) {
            return RequestError.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.toString())
                    .message("Parameter '" + this.SCOPE_PARAM + "' is required")
                    .build();
        }

        if(formData.get(this.APPLICATION_PARAM) == null || formData.get(this.APPLICATION_PARAM).isEmpty()) {
            return RequestError.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.toString())
                    .message("Parameter '" + this.APPLICATION_PARAM + "' is required")
                    .build();
        }

        if(formData.get(this.CLIENT_ID_PARAM).size() > 1) {
            return RequestError.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.toString())
                    .message("Parameter '" + this.CLIENT_ID_PARAM + "' must be unique")
                    .build();
        }

        if(formData.get(this.CLIENT_SECRET_PARAM).size() > 1) {
            return RequestError.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.toString())
                    .message("Parameter '" + this.CLIENT_SECRET_PARAM + "' must be unique")
                    .build();
        }

        if(formData.get(this.SCOPE_PARAM).size() > 1) {
            return RequestError.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.toString())
                    .message("Parameter '" + this.SCOPE_PARAM + "' must be unique")
                    .build();
        }

        if(formData.get(this.APPLICATION_PARAM).size() > 1) {
            return RequestError.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.toString())
                    .message("Parameter '" + this.APPLICATION_PARAM + "' must be unique")
                    .build();
        }

        return null;
    }
}
