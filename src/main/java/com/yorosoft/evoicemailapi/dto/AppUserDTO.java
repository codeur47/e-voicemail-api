package com.yorosoft.evoicemailapi.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.Date;

@Data
@RequiredArgsConstructor
public class AppUserDTO {
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Date lastLoginDate;
    private Date lastLoginDateDisplay;
    private Date joinDate;
    private String role;
    private String[] authorities;
    private String[] idUserSupervised;
    private boolean isActive;
    private boolean isNotLocked;
    private Integer themeId;
}
