package com.nivtron.smartmoney.controller;

import com.nivtron.smartmoney.api.UsersApi;
import com.nivtron.smartmoney.dto.BaseResponse;
import com.nivtron.smartmoney.dto.ChangePasswordRequest;
import com.nivtron.smartmoney.dto.UserProfileResponse;
import com.nivtron.smartmoney.entity.EntityUser;
import com.nivtron.smartmoney.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

  private final UserService userService;

  private String getCurrentUserEmail() {
    return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  private EntityUser getCurrentUser() {
    return userService.getUserByEmail(getCurrentUserEmail());
  }

  @Override
  public ResponseEntity<UserProfileResponse> getMyProfile() {
    return ResponseEntity.ok(userService.getProfile(getCurrentUser()));
  }

  @Override
  public ResponseEntity<BaseResponse> changePassword(ChangePasswordRequest changePasswordRequest) {
    return ResponseEntity.ok(userService.changePassword(getCurrentUser(), changePasswordRequest));
  }
}
