package com.learning.frontier.model.transport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenOutput {

    private String scope;
    private Long consentedOn;
    private String token;
    private int duration;
}
