package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.model.UserDTO;
import com.openclassrooms.mddapi.dto.request.LoginRequest;
import com.openclassrooms.mddapi.dto.request.RegisterRequest;
import com.openclassrooms.mddapi.dto.response.AuthResponse;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.security.JwtUtil;
import com.openclassrooms.mddapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for authentication endpoints.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final JwtUtil jwtUtil;
  private final UserMapper userMapper;

  /**
   * Authenticates user and returns JWT token.
   *
   * @param loginRequest login credentials
   * @return JWT token and user information
   */
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.usernameOrEmail(),
            loginRequest.password()
        )
    );

    User user = userService.findByUsername(authentication.getName())
        .orElseThrow(
            () -> new UsernameNotFoundException("User not found: " + authentication.getName()));

    String token = jwtUtil.generateToken(user.getId());

    UserDTO userDTO = userMapper.toDTO(user);
    return ResponseEntity.ok(new AuthResponse(token, userDTO));
  }

  /**
   * Registers a new user.
   *
   * @param registerRequest registration details
   * @return JWT token and user information
   */
  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(
      @Valid @RequestBody RegisterRequest registerRequest) {
    User user = new User();
    user.setUsername(registerRequest.username());
    user.setEmail(registerRequest.email());
    user.setPassword(registerRequest.password());

    // Save user (password will be encrypted by UserService)
    User savedUser = userService.registerUser(user);

    String token = jwtUtil.generateToken(savedUser.getId());

    UserDTO userDTO = userMapper.toDTO(savedUser);
    return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token, userDTO));
  }
}