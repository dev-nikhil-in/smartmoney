package com.nivtron.smartmoney.service;

import com.nivtron.smartmoney.config.JwtService;
import com.nivtron.smartmoney.dto.AuthData;
import com.nivtron.smartmoney.dto.LoginResponse;
import com.nivtron.smartmoney.entity.EntityUser;
import com.nivtron.smartmoney.entity.Role;
import com.nivtron.smartmoney.exception.BadRequestException;
import com.nivtron.smartmoney.exception.UnauthorizedException;
import com.nivtron.smartmoney.repository.UserRepository;
import com.nivtron.smartmoney.util.BaseResponseUtil;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleSSOService {

  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final BCryptPasswordEncoder passwordEncoder;
  private final RestTemplate restTemplate;

  @Value("${google.token.info.url}")
  private String googleTokenInfoUrl;

  @Value("${google.client.id}")
  private String googleClientId;

  public LoginResponse googleLogin(String idToken) {
    if (!StringUtils.hasText(idToken)) {
      throw new BadRequestException("Google ID token must not be empty");
    }

    GoogleTokenInfo tokenInfo = verifyGoogleToken(idToken);

    EntityUser user =
        userRepository
            .findByEmail(tokenInfo.getEmail())
            .orElseGet(() -> createGoogleUser(tokenInfo));

    String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

    AuthData authData = new AuthData();
    authData.setToken(token);
    authData.setType("Bearer");

    return BaseResponseUtil.loginSuccess("Login successful", authData);
  }

  private GoogleTokenInfo verifyGoogleToken(String idToken) {
    GoogleTokenInfo tokenInfo;

    try {
      tokenInfo = restTemplate.getForObject(googleTokenInfoUrl + idToken, GoogleTokenInfo.class);

    } catch (HttpClientErrorException e) {
      log.warn("Google rejected token — status: {}", e.getStatusCode());
      throw new BadRequestException("Invalid or expired Google token");

    } catch (HttpServerErrorException e) {
      log.error("Google token info API server error: {}", e.getStatusCode());
      throw new RuntimeException("Google authentication service is unavailable");

    } catch (ResourceAccessException e) {
      log.error("Could not reach Google token info API: {}", e.getMessage());
      throw new RuntimeException("Could not connect to Google. Check your network");
    }

    if (tokenInfo == null) {
      throw new BadRequestException("Invalid Google token — empty response");
    }

    if (!StringUtils.hasText(tokenInfo.getEmail())) {
      throw new UnauthorizedException("Google account does not have a verified email");
    }

    if (!Boolean.TRUE.equals(tokenInfo.isEmailVerified())) {
      throw new UnauthorizedException("Google account email is not verified");
    }

    if (!googleClientId.equals(tokenInfo.getAud())) {
      log.warn(
          "Token audience mismatch. Expected: {}, Got: {}", googleClientId, tokenInfo.getAud());
      throw new UnauthorizedException("Google token was not issued for this application");
    }

    return tokenInfo;
  }

  private EntityUser createGoogleUser(GoogleTokenInfo tokenInfo) {
    log.info("Auto-registering new Google user: {}", tokenInfo.getEmail());

    return userRepository.save(
        EntityUser.builder()
            .name(
                StringUtils.hasText(tokenInfo.getName())
                    ? tokenInfo.getName()
                    : tokenInfo.getEmail())
            .email(tokenInfo.getEmail())
            .password(passwordEncoder.encode(UUID.randomUUID().toString()))
            .role(Role.USER)
            .build());
  }
}
