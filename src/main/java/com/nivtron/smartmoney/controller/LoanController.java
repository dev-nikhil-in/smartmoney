package com.nivtron.smartmoney.controller;

import com.nivtron.smartmoney.api.LoansApi;
import com.nivtron.smartmoney.dto.BaseResponse;
import com.nivtron.smartmoney.dto.LoanListResponse;
import com.nivtron.smartmoney.dto.LoanRequest;
import com.nivtron.smartmoney.dto.LoanResponse;
import com.nivtron.smartmoney.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoanController implements LoansApi {

  private final LoanService loanService;

  private String getCurrentUserEmail() {
    return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  @Override
  public ResponseEntity<LoanListResponse> getAllLoans() {
    return ResponseEntity.ok(loanService.getAllLoans(getCurrentUserEmail()));
  }

  @Override
  public ResponseEntity<LoanResponse> getLoanById(Long id) {
    return ResponseEntity.ok(loanService.getLoanById(id, getCurrentUserEmail()));
  }

  @Override
  public ResponseEntity<LoanResponse> addLoan(LoanRequest loanRequest) {
    return ResponseEntity.ok(loanService.addLoan(loanRequest, getCurrentUserEmail()));
  }

  @Override
  public ResponseEntity<LoanResponse> updateLoan(Long id, LoanRequest loanRequest) {
    return ResponseEntity.ok(loanService.updateLoan(id, loanRequest, getCurrentUserEmail()));
  }

  @Override
  public ResponseEntity<BaseResponse> deleteLoan(Long id) {
    return ResponseEntity.ok(loanService.deleteLoan(id, getCurrentUserEmail()));
  }
}
