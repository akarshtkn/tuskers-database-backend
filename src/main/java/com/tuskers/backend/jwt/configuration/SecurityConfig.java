package com.tuskers.backend.jwt.configuration;

import com.tuskers.backend.jwt.utils.JwtAuthenticationEntryPoint;
import com.tuskers.backend.jwt.utils.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final static String[] WHITE_URL = {
            "/api/v1/auth/register/super-admin",
            "/api/v1/auth/register/user",
            "/api/v1/auth/authenticate",
    };

    private final static String[] SUPER_ADMINS_URL = {
            "/api/v1/auth/register/admin",
            "/api/v1/auth/admin/**",
            "/api/v1/player/**",
    };

    private final static String[] ADMINS_URL = {
            "/api/v1/player/**",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors(c -> c.configurationSource(corsConfigurationSource))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(request ->
                    request.requestMatchers(WHITE_URL).permitAll()
                            .requestMatchers(SUPER_ADMINS_URL).hasAnyAuthority("SUPER_ADMIN")
                            .requestMatchers(ADMINS_URL).hasAnyAuthority("ADMIN")
                            .anyRequest().authenticated())
            .exceptionHandling(exception ->
                    exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}