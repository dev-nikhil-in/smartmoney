package com.nivtron.smartmoney.util;

import com.nivtron.smartmoney.dto.AuthData;
import com.nivtron.smartmoney.dto.BaseResponse;
import com.nivtron.smartmoney.dto.LoginResponse;
import com.nivtron.smartmoney.dto.RegisterResponse;
import com.nivtron.smartmoney.dto.UserProfileData;
import com.nivtron.smartmoney.dto.UserProfileResponse;
import java.time.OffsetDateTime;
import java.util.List;

public class BaseResponseUtil {

  public static LoginResponse loginSuccess(String message, AuthData data) {
    LoginResponse res = new LoginResponse();
    res.setSuccess(true);
    res.setStatusCode(200);
    res.setMessage(message);
    res.setData(data);
    res.setErrors(null);
    res.setTimestamp(OffsetDateTime.now());
    return res;
  }

  public static RegisterResponse registerSuccess(String message) {
    RegisterResponse res = new RegisterResponse();
    res.setSuccess(true);
    res.setStatusCode(200);
    res.setMessage(message);
    res.setData(null);
    res.setErrors(null);
    res.setTimestamp(OffsetDateTime.now());
    return res;
  }

  public static BaseResponse error(int statusCode, String message, List<String> errors) {
    BaseResponse res = new BaseResponse();
    res.setSuccess(false);
    res.setStatusCode(statusCode);
    res.setMessage(message);
    res.setErrors(errors);
    res.setTimestamp(OffsetDateTime.now());
    return res;
  }

  public static UserProfileResponse userProfileSuccess(String message, UserProfileData data) {
    UserProfileResponse res = new UserProfileResponse();
    res.setSuccess(true);
    res.setStatusCode(200);
    res.setMessage(message);
    res.setData(data);
    res.setErrors(null);
    res.setTimestamp(OffsetDateTime.now());
    return res;
  }

  public static BaseResponse genericSuccess(int statusCode, String message) {
    BaseResponse res = new BaseResponse();
    res.setSuccess(true);
    res.setStatusCode(statusCode);
    res.setMessage(message);
    res.setErrors(null);
    res.setTimestamp(OffsetDateTime.now());
    return res;
  }
}
