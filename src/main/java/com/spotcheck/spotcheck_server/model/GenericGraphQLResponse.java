package com.spotcheck.spotcheck_server.model;

import lombok.Builder;

@Builder
public class GenericGraphQLResponse {
    private Integer status;
    private String message;
}
