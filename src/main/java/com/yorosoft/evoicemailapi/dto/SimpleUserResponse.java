package com.yorosoft.evoicemailapi.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class SimpleUserResponse {
    private String firstName;
    private String lastName;
}
