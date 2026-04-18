package com.nivtron.smartmoney.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleTokenInfo {

  private String email;
  private String name;
  private String aud;
  private String picture;

  @JsonProperty("email_verified")
  private Boolean emailVerified;

  public boolean isEmailVerified() {
    return Boolean.TRUE.equals(emailVerified);
  }
}
