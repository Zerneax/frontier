package com.learning.frontier.endpoints;

import com.learning.frontier.model.error.RequestError;
import com.learning.frontier.model.transport.NewAutorisationInput;
import com.learning.frontier.process.AccountProcess;
import com.learning.frontier.process.AutorisationProcess;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/autorisations")
public class AutorisationEndpoint {

    private AutorisationProcess autorisationProcess;
    private AccountProcess accountProcess;

    public AutorisationEndpoint(AutorisationProcess autorisationProcess, AccountProcess accountProcess) {
        this.autorisationProcess = autorisationProcess;
        this.accountProcess = accountProcess;
    }

    @PostMapping
    public ResponseEntity addNewAutorisation(@RequestBody NewAutorisationInput newAutorisationInput) {

        RequestError requestError = this.checkParametersAddNewAutorisation(newAutorisationInput);
        if(requestError != null) {
            return ResponseEntity.badRequest().body(requestError);
        }

        if(!this.accountProcess.checkIfAccountAlredyExist(newAutorisationInput.getApplication())) {
          RequestError requestError1 = RequestError.builder()
            .httpStatus(HttpStatus.NOT_FOUND.toString())
            .message("This application/scope don't exit")
            .build();

          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(requestError1);
        }

        if(!this.accountProcess.checkIfClientIdIsCorrect(newAutorisationInput.getApplication(), newAutorisationInput.getClientId())) {
            RequestError requestError2 = RequestError.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.toString())
                    .message("This clientId doesn't exit")
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(requestError2);
        }

        this.autorisationProcess.addNewAutorisation(newAutorisationInput);

        return ResponseEntity.ok().build();

    }

    @GetMapping
    public ResponseEntity getAutorisations() {
        return ResponseEntity.ok(this.autorisationProcess.getAutorisations());
    }

    protected RequestError checkParametersAddNewAutorisation(NewAutorisationInput newAutorisationInput) {
        if(newAutorisationInput == null) {
            return RequestError.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.toString())
                    .message("Body is required")
                    .build();
        }

        if(newAutorisationInput.getClientId() == null || StringUtils.isEmpty(newAutorisationInput.getClientId())) {
            return RequestError.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.toString())
                    .message("Parameter 'clientId' is required")
                    .build();
        }

        if(newAutorisationInput.getApplication() == null || StringUtils.isEmpty(newAutorisationInput.getApplication())) {
            return RequestError.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.toString())
                    .message("Parameter 'application' is required")
                    .build();
        }

        if(newAutorisationInput.getScope() == null || StringUtils.isEmpty(newAutorisationInput.getScope())) {
            return RequestError.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.toString())
                    .message("Parameter 'scope' is required")
                    .build();
        }

        return null;
    }
}
