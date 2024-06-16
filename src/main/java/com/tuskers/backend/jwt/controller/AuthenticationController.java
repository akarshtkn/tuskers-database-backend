package com.tuskers.backend.jwt.controller;

import com.tuskers.backend.jwt.dto.AuthenticationRequest;
import com.tuskers.backend.jwt.dto.AuthenticationResponse;
import com.tuskers.backend.jwt.dto.RegisterRequest;
import com.tuskers.backend.jwt.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register/super-admin")
    public ResponseEntity<AuthenticationResponse> registerSuperAdmin(@RequestBody RegisterRequest request) {
        return new ResponseEntity<>(authenticationService.registerSuperAdmin(request), HttpStatus.CREATED);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody RegisterRequest request) {
        return new ResponseEntity<>(authenticationService.registerAdmin(request), HttpStatus.CREATED);
    }

    @PostMapping("/register/user")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest request) {
        return new ResponseEntity<>(authenticationService.registerUser(request), HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return new ResponseEntity<>(authenticationService.authenticate(request), HttpStatus.OK);
    }

    @DeleteMapping("/register/admin/delete")
    public ResponseEntity<String> deleteAdmin(@RequestParam Integer adminId) {
        authenticationService.deleteAdmin(adminId);
        return new ResponseEntity<>("Deletion Successful", HttpStatus.OK);
    }
}
