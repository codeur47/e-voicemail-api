package com.yorosoft.evoicemailapi.service.impl;

import com.yorosoft.evoicemailapi.dto.SimpleUserResponse;
import com.yorosoft.evoicemailapi.dto.UserResponse;
import com.yorosoft.evoicemailapi.enumeration.Role;
import com.yorosoft.evoicemailapi.exception.domain.UserNotFoundException;
import com.yorosoft.evoicemailapi.exception.domain.UsernameExistException;
import com.yorosoft.evoicemailapi.model.AppUser;
import com.yorosoft.evoicemailapi.repository.UserRepository;
import com.yorosoft.evoicemailapi.security.UserPrincipal;
import com.yorosoft.evoicemailapi.service.LoginAttemptService;
import com.yorosoft.evoicemailapi.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.yorosoft.evoicemailapi.constant.UserImplConstant.*;

import static com.yorosoft.evoicemailapi.enumeration.Role.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;
@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findAppUserByUsername(username);
        if (user == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        } else {
            validateLoginAttempt(user);
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(FOUND_USER_BY_USERNAME + username);
            return userPrincipal;
        }
    }

    @Override
    public AppUser register(String firstName, String lastName, String username, Integer themeId) throws UserNotFoundException, UsernameExistException {
        validateNewUsername(EMPTY, username);
        AppUser user = new AppUser();
        user.setUserId(generateUserId());
        String password = generatePassword();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setJoinDate(new Date());
        user.setPassword(encodePassword(password));
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(ROLE_ADMIN.name());
        user.setAuthorities(ROLE_ADMIN.getAuthorities());
        user.setThemeId(themeId);
        userRepository.save(user);
        LOGGER.info("New user password: {}",password);
        return user;
    }

    @Override
    public AppUser addNewUser(String firstName, String lastName, String username, String role, boolean isNonLocked, boolean isActive, String supId, Integer themeId) throws UserNotFoundException, UsernameExistException {
        validateNewUsername(EMPTY, username);
        AppUser user = new AppUser();
        String password = generatePassword();
        user.setUserId(generateUserId());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setJoinDate(new Date());
        user.setUsername(username);
        user.setPassword(encodePassword(password));
        user.setActive(isActive);
        user.setNotLocked(isNonLocked);
        user.setRole(getRoleEnumName(role).name());
        user.setAuthorities(getRoleEnumName(role).getAuthorities());
        if (StringUtils.isNotBlank(supId))
            user.setSupId(supId);
        user.setThemeId(themeId);
        userRepository.save(user);
        LOGGER.info("New user password: {}",password);
        return user;
    }

    @Override
    public AppUser updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String role, boolean isNonLocked, boolean isActive, String supId) throws UserNotFoundException, UsernameExistException {
        AppUser currentUser = validateNewUsername(currentUsername, newUsername);
        if (currentUser != null) {
            currentUser.setFirstName(newFirstName);
            currentUser.setLastName(newLastName);
            currentUser.setUsername(newUsername);
            currentUser.setActive(isActive);
            currentUser.setNotLocked(isNonLocked);
            currentUser.setRole(getRoleEnumName(role).name());
            currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
            if (StringUtils.isNotBlank(supId))
                currentUser.setSupId(supId);
            userRepository.save(currentUser);
            return currentUser;
        }
        throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
    }

    @Override
    public void resetPassword(String username) throws UserNotFoundException {
        AppUser user = userRepository.findAppUserByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        }
        String password = generatePassword();
        user.setPassword(encodePassword(password));
        userRepository.save(user);
        LOGGER.info("New user password: {}",password);
    }

    @Override
    public List<AppUser> getUsers() {
        return userRepository.findAll();
    }


    @Override
    public List<UserResponse> getUserResponse() {
        List<UserResponse> userResponses = new ArrayList<>();
        List<AppUser> appUsers = getUsers();
        if (!appUsers.isEmpty()) {
            appUsers.forEach(appUser -> {
                LOGGER.info("Size : {}",appUsers.size());
                LOGGER.info("LastName : {}",appUser.getLastName());
            });
            appUsers.forEach(appUser -> {
                UserResponse userResponse = new UserResponse();
                userResponse.setUserId(appUser.getUserId());
                userResponse.setFirstName(appUser.getFirstName());
                userResponse.setLastName(appUser.getLastName());
                userResponse.setUsername(appUser.getUsername());
                userResponse.setLastLoginDate(appUser.getLastLoginDate());
                userResponse.setJoinDate(appUser.getJoinDate());
                userResponse.setRole(appUser.getRole());
                userResponse.setAuthorities(appUser.getAuthorities());
                userResponse.setSupId(appUser.getSupId());
                userResponse.setActive(appUser.isActive());
                userResponse.setNotLocked(appUser.isNotLocked());
                userResponse.setThemeId(appUser.getThemeId());
                if (appUser.getRole().equals(ROLE_USER.name())){
                    List<SimpleUserResponse> simpleUserResponses = new ArrayList<>();
                    simpleUserResponses.add(findSimpleUserResponseByUserId(userResponse.getSupId()));
                    userResponse.setSimpleUserResponses(simpleUserResponses);
                }
                if(appUser.getRole().equals(ROLE_SUPERVISOR.name())) {
                    List<AppUser> appUserList = userRepository.findAllAppUsersBySupId(appUser.getUserId());
                    List<SimpleUserResponse> simpleUserResponses = new ArrayList<>();
                    appUserList.forEach(appUser1 -> {
                        SimpleUserResponse simpleUserResponse = new SimpleUserResponse();
                        simpleUserResponse.setFirstName(appUser1.getFirstName());
                        simpleUserResponse.setLastName(appUser1.getLastName());
                        simpleUserResponse.setThemeId(appUser1.getThemeId());
                        simpleUserResponses.add(simpleUserResponse);
                    });
                    userResponse.setSimpleUserResponses(simpleUserResponses);
                }
                userResponses.add(userResponse);
            });
            return userResponses;
        }else {
            LOGGER.info("No users found ");
            return userResponses;
        }

    }

    @Override
    public SimpleUserResponse findSimpleUserResponseByUserId(String userId) {
        AppUser appUser = findUserByUserId(userId);
        SimpleUserResponse simpleUserResponse = new SimpleUserResponse();
        simpleUserResponse.setLastName(appUser.getLastName());
        simpleUserResponse.setFirstName(appUser.getFirstName());
        simpleUserResponse.setThemeId(appUser.getThemeId());
        return simpleUserResponse;
    }

    @Override
    public AppUser findUserByUsername(String username) {
        return userRepository.findAppUserByUsername(username);
    }

    @Override
    public AppUser findUserByUserId(String userId) {
        return userRepository.findAppUserByUserId(userId);
    }

    @Override
    public void deleteUser(String username) {
        AppUser user = userRepository.findAppUserByUsername(username);
        userRepository.deleteById(user.getId());
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private void validateLoginAttempt(AppUser user) {
        if(user.isNotLocked()) {
            user.setNotLocked(!loginAttemptService.hasExceededMaxAttempts(user.getUsername()));
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

    private AppUser validateNewUsername(String currentUsername, String newUsername) throws UserNotFoundException, UsernameExistException {
        AppUser userByNewUsername = findUserByUsername(newUsername);
        if(StringUtils.isNotBlank(currentUsername)) {
            AppUser currentUser = findUserByUsername(currentUsername);
            if(currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            if(userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if(userByNewUsername != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            return null;
        }
    }

}
