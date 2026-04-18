package com.nivtron.smartmoney.service;

import com.nivtron.smartmoney.dto.LoanData;
import com.nivtron.smartmoney.dto.LoanListResponse;
import com.nivtron.smartmoney.dto.LoanRequest;
import com.nivtron.smartmoney.dto.LoanResponse;
import com.nivtron.smartmoney.entity.EntityLoan;
import com.nivtron.smartmoney.entity.EntityUser;
import com.nivtron.smartmoney.exception.ResourceNotFoundException;
import com.nivtron.smartmoney.repository.LoanRepository;
import com.nivtron.smartmoney.util.BaseResponseUtil;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanService {

  private final LoanRepository loanRepository;
  private final UserService userService;

  public LoanListResponse getAllLoans(String email) {
    EntityUser user = userService.getUserByEmail(email);
    List<LoanData> loans =
        loanRepository.findAllByUser(user).stream().map(this::toLoanData).toList();
    return BaseResponseUtil.loanListSuccess("Loans fetched successfully", loans);
  }

  public LoanResponse getLoanById(Long id, String email) {
    EntityUser user = userService.getUserByEmail(email);
    EntityLoan loan =
        loanRepository
            .findByIdAndUser(id, user)
            .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));
    return BaseResponseUtil.loanSuccess("Loan fetched successfully", toLoanData(loan));
  }

  public LoanResponse addLoan(LoanRequest request, String email) {
    EntityUser user = userService.getUserByEmail(email);

    EntityLoan loan =
        EntityLoan.builder()
            .user(user)
            .loanName(request.getLoanName())
            .totalAmount(request.getTotalAmount())
            .outstandingAmount(request.getOutstandingAmount())
            .interestRate(request.getInterestRate())
            .emiAmount(request.getEmiAmount())
            .tenureMonths(request.getTenureMonths())
            .startDate(request.getStartDate())
            .build();

    return BaseResponseUtil.loanSuccess(
        "Loan added successfully", toLoanData(loanRepository.save(loan)));
  }

  public LoanResponse updateLoan(Long id, LoanRequest request, String email) {
    EntityUser user = userService.getUserByEmail(email);
    EntityLoan loan =
        loanRepository
            .findByIdAndUser(id, user)
            .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

    loan.setLoanName(request.getLoanName());
    loan.setTotalAmount(request.getTotalAmount());
    loan.setOutstandingAmount(request.getOutstandingAmount());
    loan.setInterestRate(request.getInterestRate());
    loan.setEmiAmount(request.getEmiAmount());
    loan.setTenureMonths(request.getTenureMonths());
    loan.setStartDate(request.getStartDate());

    return BaseResponseUtil.loanSuccess(
        "Loan updated successfully", toLoanData(loanRepository.save(loan)));
  }

  public com.nivtron.smartmoney.dto.BaseResponse deleteLoan(Long id, String email) {
    EntityUser user = userService.getUserByEmail(email);
    EntityLoan loan =
        loanRepository
            .findByIdAndUser(id, user)
            .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));
    loan.softDelete();
    loanRepository.save(loan);
    return BaseResponseUtil.genericSuccess(200, "Loan deleted successfully");
  }

  private LoanData toLoanData(EntityLoan loan) {
    LoanData data = new LoanData();
    data.setId(loan.getId());
    data.setLoanName(loan.getLoanName());
    data.setTotalAmount(loan.getTotalAmount());
    data.setOutstandingAmount(loan.getOutstandingAmount());
    data.setInterestRate(loan.getInterestRate());
    data.setEmiAmount(loan.getEmiAmount());
    data.setTenureMonths(loan.getTenureMonths());
    data.setStartDate(loan.getStartDate());
    data.setCreatedAt(loan.getCreatedAt().atOffset(ZoneOffset.UTC));
    if (loan.getUpdatedAt() != null) {
      data.setUpdatedAt(loan.getUpdatedAt().atOffset(ZoneOffset.UTC));
    }
    return data;
  }
}
