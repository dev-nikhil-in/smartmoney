package com.nivtron.smartmoney.service;

import com.nivtron.smartmoney.config.JwtService;
import com.nivtron.smartmoney.dto.LoginResponse;
import com.nivtron.smartmoney.dto.RegisterResponse;
import com.nivtron.smartmoney.entity.EntityUser;
import com.nivtron.smartmoney.entity.Role;
import com.nivtron.smartmoney.exception.BadRequestException;
import com.nivtron.smartmoney.exception.UnauthorizedException;
import com.nivtron.smartmoney.repository.UserRepository;
import com.nivtron.smartmoney.util.BaseResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final BCryptPasswordEncoder encoder;

  public RegisterResponse register(com.nivtron.smartmoney.dto.RegisterRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new BadRequestException("Email already registered");
    }

    EntityUser user =
        EntityUser.builder()
            .name(request.getName())
            .email(request.getEmail())
            .password(encoder.encode(request.getPassword()))
            .role(Role.USER) // always USER on self-register
            .build();

    userRepository.save(user);

    return BaseResponseUtil.registerSuccess("User registered successfully");
  }

  public LoginResponse login(com.nivtron.smartmoney.dto.LoginRequest request) {
    EntityUser user =
        userRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

    if (!encoder.matches(request.getPassword(), user.getPassword())) {
      throw new UnauthorizedException("Invalid credentials");
    }

    String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

    com.nivtron.smartmoney.dto.AuthData authData = new com.nivtron.smartmoney.dto.AuthData();
    authData.setToken(token);
    authData.setType("Bearer");

    return BaseResponseUtil.loginSuccess("Login successful", authData);
  }
}
