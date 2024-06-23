package com.tuskers.backend.jwt.service;

import com.tuskers.backend.jwt.dto.AuthenticationRequest;
import com.tuskers.backend.jwt.dto.AuthenticationResponse;
import com.tuskers.backend.jwt.dto.RegisterRequest;
import com.tuskers.backend.jwt.dto.UserList;
import com.tuskers.backend.jwt.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface AuthenticationService {
    AuthenticationResponse registerSuperAdmin(RegisterRequest request);

    AuthenticationResponse registerAdmin(RegisterRequest request);

    AuthenticationResponse registerUser(RegisterRequest request);

    List<UserList> getAllAdmins();

    void deleteAdmin(Integer adminId);

    User authenticate(AuthenticationRequest request);

    String generateToken(UserDetails userDetails);

    AuthenticationResponse mappingResponse(User user);
}
