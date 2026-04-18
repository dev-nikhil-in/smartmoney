package com.nivtron.smartmoney.controller;

import com.nivtron.smartmoney.api.AuthApi;
import com.nivtron.smartmoney.dto.*;
import com.nivtron.smartmoney.service.AuthService;
import com.nivtron.smartmoney.service.GoogleSSOService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

  private final AuthService authService;
  private final GoogleSSOService googleSsoService;

  @Override
  public ResponseEntity<LoginResponse> googleLogin(GoogleAuthRequest request) {
    return ResponseEntity.ok(googleSsoService.googleLogin(request.getIdToken()));
  }

  @Override
  public ResponseEntity<LoginResponse> loginUser(LoginRequest loginRequest) {
    return ResponseEntity.ok(authService.login(loginRequest));
  }

  @Override
  public ResponseEntity<RegisterResponse> registerUser(RegisterRequest registerRequest) {
    return ResponseEntity.ok(authService.register(registerRequest));
  }
}
