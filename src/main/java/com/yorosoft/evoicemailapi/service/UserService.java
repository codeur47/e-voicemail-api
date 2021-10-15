package com.yorosoft.evoicemailapi.service;


import com.yorosoft.evoicemailapi.exception.domain.UserNotFoundException;
import com.yorosoft.evoicemailapi.exception.domain.UsernameExistException;
import com.yorosoft.evoicemailapi.model.AppUser;
import java.util.List;

public interface UserService {

    AppUser register(String firstName, String lastName, String username) throws UserNotFoundException, UsernameExistException;

    List<AppUser> getUsers();

    AppUser findUserByUsername(String username);

    AppUser addNewUser(String firstName, String lastName, String username, String role, boolean isNonLocked, boolean isActive) throws UserNotFoundException, UsernameExistException;

    AppUser updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String role, boolean isNonLocked, boolean isActive) throws UserNotFoundException, UsernameExistException;

    void deleteUser(String username);

    void resetPassword(String username) throws UserNotFoundException;

}
