package com.yorosoft.evoicemailapi.repository;

import com.yorosoft.evoicemailapi.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findAppUserByUsername(String username);
}
