package com.nivtron.smartmoney.service;

import com.nivtron.smartmoney.dto.SimulateData;
import com.nivtron.smartmoney.dto.SimulateRequest;
import com.nivtron.smartmoney.dto.SimulateResponse;
import com.nivtron.smartmoney.entity.EntityLoan;
import com.nivtron.smartmoney.entity.EntityUser;
import com.nivtron.smartmoney.exception.ResourceNotFoundException;
import com.nivtron.smartmoney.repository.LoanRepository;
import com.nivtron.smartmoney.util.BaseResponseUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimulatorService {

  private final LoanRepository loanRepository;
  private final UserService userService;

  public SimulateResponse simulate(Long loanId, SimulateRequest request, String email) {
    EntityUser user = userService.getUserByEmail(email);
    EntityLoan loan =
        loanRepository
            .findByIdAndUser(loanId, user)
            .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

    BigDecimal outstanding = loan.getOutstandingAmount();
    BigDecimal annualRate = loan.getInterestRate();
    BigDecimal emi = loan.getEmiAmount();
    BigDecimal extra = request.getExtraMonthlyPayment();
    LocalDate startDate = loan.getStartDate();

    // Monthly interest rate
    BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);

    // Original months remaining
    int originalMonths = calculateMonthsToClose(outstanding, emi, monthlyRate);

    // New months with extra payment
    BigDecimal newEmi = emi.add(extra);
    int newMonths = calculateMonthsToClose(outstanding, newEmi, monthlyRate);

    int monthsSaved = originalMonths - newMonths;

    // Interest saved
    BigDecimal originalTotalPayment = emi.multiply(BigDecimal.valueOf(originalMonths));
    BigDecimal newTotalPayment = newEmi.multiply(BigDecimal.valueOf(newMonths));
    BigDecimal interestSaved =
        originalTotalPayment.subtract(newTotalPayment).setScale(2, RoundingMode.HALF_UP);

    // Closure dates
    LocalDate originalClosureDate = startDate.plusMonths(originalMonths);
    LocalDate newClosureDate = startDate.plusMonths(newMonths);

    SimulateData data = new SimulateData();
    data.setOriginalClosureDate(originalClosureDate);
    data.setNewClosureDate(newClosureDate);
    data.setMonthsSaved(monthsSaved);
    data.setInterestSaved(interestSaved);
    data.setNewMonthlyPayment(newEmi);

    return BaseResponseUtil.simulateSuccess("Simulation completed successfully", data);
  }

  private int calculateMonthsToClose(
      BigDecimal outstanding, BigDecimal emi, BigDecimal monthlyRate) {
    if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
      return outstanding.divide(emi, 0, RoundingMode.CEILING).intValue();
    }

    // n = -log(1 - (P*r/EMI)) / log(1+r)
    double P = outstanding.doubleValue();
    double r = monthlyRate.doubleValue();
    double e = emi.doubleValue();

    double numerator = Math.log(1 - (P * r / e));
    double denominator = Math.log(1 + r);

    return (int) Math.ceil(-numerator / denominator);
  }
}
