package com.yorosoft.evoicemailapi.controller;

import com.yorosoft.evoicemailapi.dto.UserRequest;
import com.yorosoft.evoicemailapi.dto.UserResponse;
import com.yorosoft.evoicemailapi.exception.ExceptionHandling;
import com.yorosoft.evoicemailapi.exception.domain.UserNotFoundException;
import com.yorosoft.evoicemailapi.exception.domain.UsernameExistException;
import com.yorosoft.evoicemailapi.model.AppUser;
import com.yorosoft.evoicemailapi.security.JWTTokenProvider;
import com.yorosoft.evoicemailapi.security.UserPrincipal;
import com.yorosoft.evoicemailapi.service.UserService;
import com.yorosoft.evoicemailapi.utility.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.yorosoft.evoicemailapi.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = { "/", "/user"})
public class UserController extends ExceptionHandling {

    public static final String EMAIL_SENT = "An email with a new password was sent to: ";
    public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, UserService userService, JWTTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<AppUser> login(@RequestBody AppUser user) {
        authenticate(user.getUsername(), user.getPassword());
        AppUser loginUser = userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }

    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@RequestBody AppUser user) throws UserNotFoundException, UsernameExistException {
        AppUser newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getThemeId());
        return new ResponseEntity<>(newUser, OK);
    }

    @PostMapping("/add")
    public ResponseEntity<AppUser> addNewUser(@RequestBody UserRequest userRequest) throws UserNotFoundException, UsernameExistException {
        AppUser newUser = userService.addNewUser(userRequest.getFirstName(), userRequest.getLastName(), userRequest.getUsername(), userRequest.getRole(), userRequest.isNotLocked(), userRequest.isActive(),userRequest.getSupId(), userRequest.getThemeId());
        return new ResponseEntity<>(newUser, OK);
    }

    @PostMapping("/update")
    public ResponseEntity<AppUser> update(@RequestBody UserRequest userRequest) throws UserNotFoundException, UsernameExistException {
        AppUser updatedUser = userService.updateUser(userRequest.getOldUsername(), userRequest.getFirstName(), userRequest.getLastName(), userRequest.getUsername(), userRequest.getRole(), userRequest.isNotLocked(), userRequest.isActive(),userRequest.getSupId());
        return new ResponseEntity<>(updatedUser, OK);
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<AppUser> getUser(@PathVariable("username") String username) {
        AppUser user = userService.findUserByUsername(username);
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getUserResponse();
        return new ResponseEntity<>(users, OK);
    }

    @GetMapping("/resetpassword/{username}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("username") String username) throws UserNotFoundException {
        userService.resetPassword(username);
        return response(OK, EMAIL_SENT + username);
    }

    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("username") String username) {
        userService.deleteUser(username);
        return response(OK, USER_DELETED_SUCCESSFULLY);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
