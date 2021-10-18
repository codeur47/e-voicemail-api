package com.yorosoft.evoicemailapi.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Date;

@Data
@Builder
@RequiredArgsConstructor
public class UserResponse {
    private String userId;
    private String firstName;
    private String lastName;
    private String username;
    private Date lastLoginDate;
    private Date joinDate;
    private String role;
    private String[] authorities;
    private String supId;
    private boolean isActive;
    private boolean isNotLocked;
    private Collection<SimpleUserResponse> simpleUserResponses;
}
