package com.nivtron.smartmoney.controller;

import com.nivtron.smartmoney.api.AuthApi;
import com.nivtron.smartmoney.dto.LoginRequest;
import com.nivtron.smartmoney.dto.LoginResponse;
import com.nivtron.smartmoney.dto.RegisterRequest;
import com.nivtron.smartmoney.dto.RegisterResponse;
import com.nivtron.smartmoney.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

  private final AuthService authService;

  @Override
  public ResponseEntity<LoginResponse> loginUser(LoginRequest loginRequest) {
    return ResponseEntity.ok(authService.login(loginRequest));
  }

  @Override
  public ResponseEntity<RegisterResponse> registerUser(RegisterRequest registerRequest) {
    return ResponseEntity.ok(authService.register(registerRequest));
  }
}
