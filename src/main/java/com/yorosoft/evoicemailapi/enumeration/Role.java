package com.yorosoft.evoicemailapi.enumeration;

import static com.yorosoft.evoicemailapi.constant.Authority.*;

public enum Role {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_SUPERVISOR(SUPERVISOR_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES);

    private String[] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}
