package com.nivtron.smartmoney.service;

import com.nivtron.smartmoney.dto.PrioritizedLoan;
import com.nivtron.smartmoney.dto.StrategyData;
import com.nivtron.smartmoney.dto.StrategyResponse;
import com.nivtron.smartmoney.entity.EntityLoan;
import com.nivtron.smartmoney.entity.EntityUser;
import com.nivtron.smartmoney.exception.BadRequestException;
import com.nivtron.smartmoney.repository.LoanRepository;
import com.nivtron.smartmoney.util.BaseResponseUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StrategyService {

  private final LoanRepository loanRepository;
  private final UserService userService;

  public StrategyResponse getAvalancheStrategy(String email) {
    EntityUser user = userService.getUserByEmail(email);
    List<EntityLoan> loans = loanRepository.findAllByUser(user);

    if (loans.isEmpty()) {
      throw new BadRequestException("No loans found to generate strategy");
    }

    // Sort by highest interest rate first
    List<EntityLoan> sorted =
        loans.stream()
            .sorted(Comparator.comparing(EntityLoan::getInterestRate).reversed())
            .toList();

    return buildStrategyResponse(sorted, "AVALANCHE");
  }

  public StrategyResponse getSnowballStrategy(String email) {
    EntityUser user = userService.getUserByEmail(email);
    List<EntityLoan> loans = loanRepository.findAllByUser(user);

    if (loans.isEmpty()) {
      throw new BadRequestException("No loans found to generate strategy");
    }

    // Sort by smallest outstanding amount first
    List<EntityLoan> sorted =
        loans.stream().sorted(Comparator.comparing(EntityLoan::getOutstandingAmount)).toList();

    return buildStrategyResponse(sorted, "SNOWBALL");
  }

  private StrategyResponse buildStrategyResponse(List<EntityLoan> sorted, String strategyType) {
    EntityLoan topLoan = sorted.get(0);

    AtomicInteger counter = new AtomicInteger(1);
    List<PrioritizedLoan> prioritizedLoans =
        sorted.stream()
            .map(loan -> toPrioritizedLoan(loan, counter.getAndIncrement(), strategyType))
            .toList();

    // Estimate interest saved & months earlier if top loan closed first
    BigDecimal interestSaved = calculateInterestSaved(topLoan);
    int monthsEarlier = calculateMonthsEarlier(topLoan);

    String recommendation = "Close \"" + topLoan.getLoanName() + "\" first";

    StrategyData data = new StrategyData();
    data.setStrategy(StrategyData.StrategyEnum.valueOf(strategyType));
    data.setRecommendation(recommendation);
    data.setInterestSaved(interestSaved);
    data.setMonthsEarlier(monthsEarlier);
    data.setPrioritizedLoans(prioritizedLoans);

    return BaseResponseUtil.strategySuccess(strategyType + " strategy fetched successfully", data);
  }

  private PrioritizedLoan toPrioritizedLoan(EntityLoan loan, int priority, String strategyType) {
    PrioritizedLoan p = new PrioritizedLoan();
    p.setPriority(priority);
    p.setLoanId(loan.getId());
    p.setLoanName(loan.getLoanName());
    p.setOutstandingAmount(loan.getOutstandingAmount());
    p.setInterestRate(loan.getInterestRate());
    p.setEmiAmount(loan.getEmiAmount());
    p.setReason(getReason(loan, priority, strategyType));
    return p;
  }

  private String getReason(EntityLoan loan, int priority, String strategyType) {
    if (priority == 1) {
      return strategyType.equals("AVALANCHE")
          ? "Highest interest rate — close this first to save maximum interest"
          : "Smallest outstanding amount — close this first for quick win";
    }
    return strategyType.equals("AVALANCHE")
        ? "Interest rate: " + loan.getInterestRate() + "%"
        : "Outstanding: ₹" + loan.getOutstandingAmount();
  }

  // Simple estimation: interest saved = outstanding * rate/100 * (tenureMonths/12) * 0.3
  private BigDecimal calculateInterestSaved(EntityLoan loan) {
    return loan.getOutstandingAmount()
        .multiply(loan.getInterestRate())
        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(loan.getTenureMonths()))
        .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(0.3))
        .setScale(2, RoundingMode.HALF_UP);
  }

  // Estimate months earlier = tenureMonths * 0.2
  private int calculateMonthsEarlier(EntityLoan loan) {
    return (int) Math.round(loan.getTenureMonths() * 0.2);
  }
}
