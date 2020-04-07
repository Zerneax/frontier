package com.learning.frontier.model.transport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewAutorisationInput {

    private String clientId;
    private String application;
    private String scope;
}
