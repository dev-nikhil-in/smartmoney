package com.nivtron.smartmoney.controller;

import com.nivtron.smartmoney.api.CalculatorsApi;
import com.nivtron.smartmoney.dto.EmiCalculatorRequest;
import com.nivtron.smartmoney.dto.EmiCalculatorResponse;
import com.nivtron.smartmoney.dto.LumpsumCalculatorRequest;
import com.nivtron.smartmoney.dto.LumpsumCalculatorResponse;
import com.nivtron.smartmoney.dto.SipCalculatorRequest;
import com.nivtron.smartmoney.dto.SipCalculatorResponse;
import com.nivtron.smartmoney.service.CalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CalculatorController implements CalculatorsApi {

  private final CalculatorService calculatorService;

  @Override
  public ResponseEntity<EmiCalculatorResponse> calculateEmi(EmiCalculatorRequest request) {
    return ResponseEntity.ok(calculatorService.calculateEmi(request));
  }

  @Override
  public ResponseEntity<SipCalculatorResponse> calculateSip(SipCalculatorRequest request) {
    return ResponseEntity.ok(calculatorService.calculateSip(request));
  }

  @Override
  public ResponseEntity<LumpsumCalculatorResponse> calculateLumpsum(
      LumpsumCalculatorRequest request) {
    return ResponseEntity.ok(calculatorService.calculateLumpsum(request));
  }
}
