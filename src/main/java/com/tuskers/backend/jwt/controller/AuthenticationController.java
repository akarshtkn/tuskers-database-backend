package com.tuskers.backend.jwt.controller;

import com.tuskers.backend.jwt.dto.AuthenticationRequest;
import com.tuskers.backend.jwt.dto.AuthenticationResponse;
import com.tuskers.backend.jwt.dto.RegisterRequest;
import com.tuskers.backend.jwt.dto.UserList;
import com.tuskers.backend.jwt.entity.User;
import com.tuskers.backend.jwt.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Value("${spring.security.jwt.cookieExpiry}")
    private long cookieExpiry;

    private final AuthenticationService authenticationService;

    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/register/super-admin")
    public ResponseEntity<AuthenticationResponse> registerSuperAdmin(@RequestBody RegisterRequest request) {
        logger.info("Register super-admin endpoint");
        return new ResponseEntity<>(authenticationService.registerSuperAdmin(request), HttpStatus.CREATED);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody RegisterRequest request) {
        logger.info("Register admin endpoint");
        return new ResponseEntity<>(authenticationService.registerAdmin(request), HttpStatus.CREATED);
    }

    @PostMapping("/register/user")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest request) {
        logger.info("Register user endpoint");
        return new ResponseEntity<>(authenticationService.registerUser(request), HttpStatus.CREATED);
    }

    @GetMapping("/admin/getAll")
    public ResponseEntity<List<UserList>> getAllAdmins() {
        logger.info("Fetching all admins endpoint");
        return new ResponseEntity<>(authenticationService.getAllAdmins(), HttpStatus.OK);
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<HttpStatus> deleteAdmin(@RequestParam Integer adminId) {
        logger.info("Admin deletion endpoint");
        authenticationService.deleteAdmin(adminId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request,
                                                               HttpServletResponse response) {
        logger.info("Authentication endpoint");
        User user = authenticationService.authenticate(request);
        String token = authenticationService.generateToken(user);

        logger.info("Executing business logic to set the cookie in response header");
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false)
                .path("/api/v1")
                .maxAge(cookieExpiry)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return new ResponseEntity<>(authenticationService.mappingResponse(user), HttpStatus.OK);
    }

}
