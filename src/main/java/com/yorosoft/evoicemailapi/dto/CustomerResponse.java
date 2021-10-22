package com.yorosoft.evoicemailapi.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CustomerResponse {
    private Long id;
    private String lastName;
    private String firstName;
    private String phoneNumber;
}
