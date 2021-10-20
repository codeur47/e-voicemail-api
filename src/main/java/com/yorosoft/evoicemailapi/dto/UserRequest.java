package com.yorosoft.evoicemailapi.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String role;
    private boolean isActive;
    private boolean isNotLocked;
    private String supId;
    private Integer themeId;
}
