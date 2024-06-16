package com.tuskers.backend.jwt.service;

import com.tuskers.backend.email.dto.EmailDto;
import com.tuskers.backend.email.service.EmailService;
import com.tuskers.backend.jwt.dto.AuthenticationRequest;
import com.tuskers.backend.jwt.dto.AuthenticationResponse;
import com.tuskers.backend.jwt.dto.RegisterRequest;
import com.tuskers.backend.jwt.entity.User;
import com.tuskers.backend.jwt.enums.Role;
import com.tuskers.backend.jwt.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationResponse registerSuperAdmin(RegisterRequest request) {
        logger.debug("inside register super admin function");
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(Role.SUPER_ADMIN)
                .build();
        User newUser = userRepository.save(user);
        logger.info("User created with id:{}", newUser.getId());
        String token = jwtService.generateToken(user);
        logger.info("Token generated toke:{}", token);
        sendEmail(newUser.getUsername(), request.getPassword(), newUser.getName());
        return mappingResponse(newUser, token);
    }

    public AuthenticationResponse registerAdmin(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(Role.ADMIN)
                .build();
        User newUser = userRepository.save(user);
        String token = jwtService.generateToken(user);
        sendEmail(newUser.getUsername(), request.getPassword(), newUser.getName());
        return mappingResponse(newUser, token);
    }

    public AuthenticationResponse registerUser(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(Role.USER)
                .build();
        User newUser = userRepository.save(user);
        String token = jwtService.generateToken(user);
        sendEmail(newUser.getUsername(), request.getPassword(), newUser.getName());
        return mappingResponse(newUser, token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = jwtService.generateToken(user);
        return mappingResponse(user, token);
    }

    private AuthenticationResponse mappingResponse(User user, String token) {
        return AuthenticationResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .token(token)
                .build();
    }

    private void sendEmail(String username, String password, String name) {
        logger.info("creating email object");
        EmailDto emailDto = EmailDto.builder()
                .toEmail(username)
                .subject("TUSKERS DASHBOARD")
                .content("Hi " + name + "\n"
                        + "Welcome to Tuskers player management Dashboard. You have be successfully registered with credentials" + "\n\n"
                        + "Username : " + username + "\n"
                        + "Password : " + password +"\n\n\n"
                        + "Thank you")
                .build();
        logger.info("email object created");

        try {
            emailService.sendMail(emailDto);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteAdmin(Integer adminId) {
        if(userRepository.findById(adminId).isPresent()){
            userRepository.deleteById(adminId);
        }
    }
}
