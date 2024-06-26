package com.tuskers.backend.jwt.service.Implementation;

import com.tuskers.backend.email.dto.EmailDto;
import com.tuskers.backend.email.service.EmailService;
import com.tuskers.backend.jwt.dto.AuthenticationRequest;
import com.tuskers.backend.jwt.dto.AuthenticationResponse;
import com.tuskers.backend.jwt.dto.RegisterRequest;
import com.tuskers.backend.jwt.dto.UserList;
import com.tuskers.backend.jwt.entity.User;
import com.tuskers.backend.jwt.enums.Role;
import com.tuskers.backend.jwt.repository.UserRepository;
import com.tuskers.backend.jwt.service.AuthenticationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtServiceImpl jwtServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    public AuthenticationResponse registerSuperAdmin(RegisterRequest request) {
        logger.info("Executing business logic for register super admin endpoint");
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(Role.SUPER_ADMIN)
                .build();
        return saveUser(user, request);
    }

    public AuthenticationResponse registerAdmin(RegisterRequest request) {
        logger.info("Executing business logic for register admin endpoint");
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(Role.ADMIN)
                .build();
        return saveUser(user, request);
    }

    public AuthenticationResponse registerUser(RegisterRequest request) {
        logger.info("Executing business logic for register user endpoint");
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(Role.USER)
                .build();
        return saveUser(user, request);
    }

    private AuthenticationResponse saveUser(User user, RegisterRequest request) {
        User newUser = userRepository.save(user);
        logger.info("User saved to repository with id:{}", user.getId());

        sendEmail(newUser.getUsername(), request.getPassword(), newUser.getName());
        return mappingResponse(newUser);
    }

    public User authenticate(AuthenticationRequest request) {
        logger.info("Executing business logic to authenticate the user");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        logger.info("Executing business logic to fetch user details of user with username :{}", request.getUsername());
        return userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void deleteAdmin(Integer adminId) {
        logger.info("Executing business logic to delete admin");
        try{
            if(userRepository.findById(adminId).isPresent()){
                userRepository.deleteById(adminId);
                logger.info("Admin deleted with id :{}", adminId);
            }
        }catch(Exception e) {
            logger.error("Could not execute deletion due to exception :{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    public List<UserList> getAllAdmins() {
        logger.info("Executing business logic to get all the admins in database");
        return userRepository.findAllAdmins();
    }

    public AuthenticationResponse mappingResponse(User user) {
        logger.info("Executing business logic to map User to AuthenticationResponse class");
        return AuthenticationResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }

    public String generateToken(UserDetails userDetails) {
        logger.info("Executing business logic to generate token with user details");
        return jwtServiceImpl.generateToken(userDetails);
    }

    private void sendEmail(String username, String password, String name) {
        logger.info("Executing business logic to send welcome mail");
        logger.info("Creating email dto");

        EmailDto emailDto = EmailDto.builder()
                .toEmail(username)
                .subject("TUSKERS DASHBOARD")
                .content("Hi " + name + "\n"
                        + "Welcome to Tuskers player management Dashboard. You have be successfully registered with credentials" + "\n\n"
                        + "Username : " + username + "\n"
                        + "Password : " + password +"\n\n\n"
                        + "Thank you")
                .build();

        try {
            emailService.sendMail(emailDto);
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("Cannot send mail due to exception :{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
