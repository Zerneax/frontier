package com.learning.frontier.endpoints;

import com.learning.frontier.model.error.RequestError;
import com.learning.frontier.model.transport.NewAccountInput;
import com.learning.frontier.process.AccountProcess;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/account")
public class AccountEndpoint {

    private AccountProcess accountProcess;

    public AccountEndpoint(AccountProcess accountProcess) {
        this.accountProcess = accountProcess;
    }

    @PostMapping
    public ResponseEntity createNewAccount(@RequestBody NewAccountInput newAccountInput) {

        RequestError requestError = this.checkParametersCreateNewAccount(newAccountInput);

        if(requestError != null) {
            return ResponseEntity.badRequest().body(requestError);
        }

        if (this.accountProcess.checkIfAccountAlredyExist(newAccountInput)) {
            RequestError requestErrorExisting = RequestError.builder()
                    .httpStatus(HttpStatus.CONFLICT.toString())
                    .message("An account is already register for this application and this scope")
                    .build();

            return ResponseEntity.status(HttpStatus.CONFLICT).body(requestErrorExisting);
        }

        return ResponseEntity.ok(this.accountProcess.createNewAccount(newAccountInput));
    }


    private RequestError checkParametersCreateNewAccount(NewAccountInput newAccountInput) {
        if(newAccountInput == null) {
            return RequestError.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.toString())
                    .message("Body is required for this path")
                    .build();
        }

        if(newAccountInput.getApplication() == null || StringUtils.isEmpty(newAccountInput.getApplication())) {
            return RequestError.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.toString())
                    .message("Parameter 'application' is required")
                    .build();
        }

        if(newAccountInput.getScope() == null || StringUtils.isEmpty(newAccountInput.getScope())) {
            return RequestError.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.toString())
                    .message("Parameter 'scope' is required")
                    .build();
        }

        return null;
    }
}
