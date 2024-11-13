package com.amazon.test_task.auth.controller;


import com.amazon.test_task.auth.dto.CreateUserDto;
import com.amazon.test_task.auth.dto.JwtResponseDto;
import com.amazon.test_task.auth.dto.LoginUserDto;
import com.amazon.test_task.auth.jwt.JwtUtils;
import com.amazon.test_task.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// REST controller for handling user authentication and registration requests.
@RestController
@RequestMapping("api/open/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final UserService userService; // Service for managing user data.
    private final JwtUtils jwtUtils; // Utility class for generating JWT tokens.
    private final AuthenticationManager authenticationManager; // Manager for authenticating user credentials.

    // Endpoint for logging in users and returning a JWT token if successful.
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody @Valid LoginUserDto loginUserDto) {
        // Authenticate user credentials.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword())
        );
        // Generate JWT for the authenticated user.
        String jwt = jwtUtils.generateToken(userService.findByEmail(loginUserDto.getEmail()));
        return ResponseEntity.ok(new JwtResponseDto(jwt));
    }

    // Endpoint for registering new users and returning a JWT token if successful.
    @PostMapping("/register")
    public ResponseEntity<JwtResponseDto> register(@RequestBody @Valid CreateUserDto createUserDto) {
        // Create a new user account.
        userService.createUser(createUserDto);
        // Generate JWT for the new user.
        String jwt = jwtUtils.generateToken(userService.findByEmail(createUserDto.getEmail()));
        return ResponseEntity.status(HttpStatus.CREATED).body(new JwtResponseDto(jwt));
    }
}
