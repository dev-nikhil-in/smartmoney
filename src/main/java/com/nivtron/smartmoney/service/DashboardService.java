package com.nivtron.smartmoney.service;

import com.nivtron.smartmoney.dto.DashboardData;
import com.nivtron.smartmoney.dto.DashboardResponse;
import com.nivtron.smartmoney.dto.HighInterestAlert;
import com.nivtron.smartmoney.dto.LoanSummary;
import com.nivtron.smartmoney.entity.EntityLoan;
import com.nivtron.smartmoney.entity.EntityUser;
import com.nivtron.smartmoney.repository.LoanRepository;
import com.nivtron.smartmoney.util.BaseResponseUtil;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

  private final LoanRepository loanRepository;
  private final UserService userService;

  public DashboardResponse getDashboard(String email) {
    EntityUser user = userService.getUserByEmail(email);
    List<EntityLoan> loans = loanRepository.findAllByUser(user);

    BigDecimal totalOutstanding =
        loans.stream()
            .map(EntityLoan::getOutstandingAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal monthlyEmi =
        loans.stream().map(EntityLoan::getEmiAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

    HighInterestAlert highInterestAlert =
        loans.stream()
            .max(Comparator.comparing(EntityLoan::getInterestRate))
            .map(
                loan -> {
                  HighInterestAlert alert = new HighInterestAlert();
                  alert.setLoanId(loan.getId());
                  alert.setLoanName(loan.getLoanName());
                  alert.setInterestRate(loan.getInterestRate());
                  alert.setOutstandingAmount(loan.getOutstandingAmount());
                  return alert;
                })
            .orElse(null);

    List<LoanSummary> loanBreakdown =
        loans.stream()
            .map(
                loan -> {
                  LoanSummary summary = new LoanSummary();
                  summary.setId(loan.getId());
                  summary.setLoanName(loan.getLoanName());
                  summary.setOutstandingAmount(loan.getOutstandingAmount());
                  summary.setInterestRate(loan.getInterestRate());
                  summary.setEmiAmount(loan.getEmiAmount());
                  return summary;
                })
            .toList();

    DashboardData data = new DashboardData();
    data.setTotalOutstanding(totalOutstanding);
    data.setMonthlyEmi(monthlyEmi);
    data.setTotalLoans(loans.size());
    data.setHighInterestAlert(highInterestAlert);
    data.setLoanBreakdown(loanBreakdown);

    return BaseResponseUtil.dashboardSuccess("Dashboard fetched successfully", data);
  }
}
