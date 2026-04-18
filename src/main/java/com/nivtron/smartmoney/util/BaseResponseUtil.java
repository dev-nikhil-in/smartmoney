package com.nivtron.smartmoney.util;

import com.nivtron.smartmoney.dto.*;
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

  public static LoanResponse loanSuccess(String message, LoanData data) {
    LoanResponse res = new LoanResponse();
    res.setSuccess(true);
    res.setStatusCode(200);
    res.setMessage(message);
    res.setData(data);
    res.setErrors(null);
    res.setTimestamp(OffsetDateTime.now());
    return res;
  }

  public static LoanListResponse loanListSuccess(String message, List<LoanData> data) {
    LoanListResponse res = new LoanListResponse();
    res.setSuccess(true);
    res.setStatusCode(200);
    res.setMessage(message);
    res.setData(data);
    res.setErrors(null);
    res.setTimestamp(OffsetDateTime.now());
    return res;
  }

  public static DashboardResponse dashboardSuccess(String message, DashboardData data) {
    DashboardResponse res = new DashboardResponse();
    res.setSuccess(true);
    res.setStatusCode(200);
    res.setMessage(message);
    res.setData(data);
    res.setErrors(null);
    res.setTimestamp(OffsetDateTime.now());
    return res;
  }

  public static StrategyResponse strategySuccess(String message, StrategyData data) {
    StrategyResponse res = new StrategyResponse();
    res.setSuccess(true);
    res.setStatusCode(200);
    res.setMessage(message);
    res.setData(data);
    res.setErrors(null);
    res.setTimestamp(OffsetDateTime.now());
    return res;
  }

  public static SimulateResponse simulateSuccess(String message, SimulateData data) {
    SimulateResponse res = new SimulateResponse();
    res.setSuccess(true);
    res.setStatusCode(200);
    res.setMessage(message);
    res.setData(data);
    res.setErrors(null);
    res.setTimestamp(OffsetDateTime.now());
    return res;
  }

  public static EmiCalculatorResponse emiSuccess(String message, EmiCalculatorData data) {
    EmiCalculatorResponse res = new EmiCalculatorResponse();
    res.setSuccess(true);
    res.setStatusCode(200);
    res.setMessage(message);
    res.setData(data);
    res.setErrors(null);
    res.setTimestamp(OffsetDateTime.now());
    return res;
  }

  public static SipCalculatorResponse sipSuccess(String message, SipCalculatorData data) {
    SipCalculatorResponse res = new SipCalculatorResponse();
    res.setSuccess(true);
    res.setStatusCode(200);
    res.setMessage(message);
    res.setData(data);
    res.setErrors(null);
    res.setTimestamp(OffsetDateTime.now());
    return res;
  }

  public static LumpsumCalculatorResponse lumpsumSuccess(
      String message, LumpsumCalculatorData data) {
    LumpsumCalculatorResponse res = new LumpsumCalculatorResponse();
    res.setSuccess(true);
    res.setStatusCode(200);
    res.setMessage(message);
    res.setData(data);
    res.setErrors(null);
    res.setTimestamp(OffsetDateTime.now());
    return res;
  }
}
