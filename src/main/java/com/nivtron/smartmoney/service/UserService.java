package com.nivtron.smartmoney.service;

import com.nivtron.smartmoney.dto.BaseResponse;
import com.nivtron.smartmoney.dto.UserProfileData;
import com.nivtron.smartmoney.entity.EntityUser;
import com.nivtron.smartmoney.exception.BadRequestException;
import com.nivtron.smartmoney.exception.ResourceNotFoundException;
import com.nivtron.smartmoney.repository.UserRepository;
import com.nivtron.smartmoney.util.BaseResponseUtil;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder encoder;

  public EntityUser getUserByEmail(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  public com.nivtron.smartmoney.dto.UserProfileResponse getProfile(EntityUser user) {
    return BaseResponseUtil.userProfileSuccess(
        "User profile fetched successfully", toProfileData(user));
  }

  public BaseResponse changePassword(
      EntityUser user, com.nivtron.smartmoney.dto.ChangePasswordRequest request) {
    if (!encoder.matches(request.getCurrentPassword(), user.getPassword())) {
      throw new BadRequestException("Current password is incorrect");
    }
    user.setPassword(encoder.encode(request.getNewPassword()));
    userRepository.save(user);
    return BaseResponseUtil.genericSuccess(200, "Password changed successfully");
  }

  private UserProfileData toProfileData(EntityUser user) {
    UserProfileData data = new UserProfileData();
    data.setId(user.getId());
    data.setName(user.getName());
    data.setEmail(user.getEmail());
    data.setRole(UserProfileData.RoleEnum.valueOf(user.getRole().name()));
    data.setCreatedAt(user.getCreatedAt().atOffset(ZoneOffset.UTC));
    if (user.getUpdatedAt() != null) {
      data.setUpdatedAt(user.getUpdatedAt().atOffset(ZoneOffset.UTC));
    }
    return data;
  }
}
