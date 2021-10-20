package com.yorosoft.evoicemailapi.dto;

import lombok.*;

import java.util.Collection;
import java.util.Date;

@Getter
@Setter
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
    private Integer themeId;
}
