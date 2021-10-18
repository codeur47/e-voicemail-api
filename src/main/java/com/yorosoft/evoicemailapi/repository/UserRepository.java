package com.yorosoft.evoicemailapi.repository;

import com.yorosoft.evoicemailapi.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findAppUserByUsername(String username);
    AppUser findAppUserByUserId(String userId);
    List<AppUser> findAllByUserId(String userId);
}
