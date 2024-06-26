package com.tuskers.backend.jwt.dto;

import com.tuskers.backend.jwt.enums.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private String username;
    private String name;
    private Role role;
}
