package com.nivtron.smartmoney.service;

import com.nivtron.smartmoney.dto.EmiCalculatorData;
import com.nivtron.smartmoney.dto.EmiCalculatorRequest;
import com.nivtron.smartmoney.dto.EmiCalculatorResponse;
import com.nivtron.smartmoney.dto.LumpsumCalculatorData;
import com.nivtron.smartmoney.dto.LumpsumCalculatorRequest;
import com.nivtron.smartmoney.dto.LumpsumCalculatorResponse;
import com.nivtron.smartmoney.dto.SipCalculatorData;
import com.nivtron.smartmoney.dto.SipCalculatorRequest;
import com.nivtron.smartmoney.dto.SipCalculatorResponse;
import com.nivtron.smartmoney.util.BaseResponseUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

  public EmiCalculatorResponse calculateEmi(EmiCalculatorRequest request) {
    BigDecimal principal = request.getLoanAmount();
    BigDecimal annualRate = request.getInterestRate();
    int tenureMonths = request.getTenureMonths();

    BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);

    BigDecimal emi;

    if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
      emi = principal.divide(BigDecimal.valueOf(tenureMonths), 2, RoundingMode.HALF_UP);
    } else {
      // EMI = P * r * (1+r)^n / ((1+r)^n - 1)
      double r = monthlyRate.doubleValue();
      double onePlusRPowN = Math.pow(1 + r, tenureMonths);
      double emiDouble = principal.doubleValue() * r * onePlusRPowN / (onePlusRPowN - 1);
      emi = BigDecimal.valueOf(emiDouble).setScale(2, RoundingMode.HALF_UP);
    }

    BigDecimal totalPayment =
        emi.multiply(BigDecimal.valueOf(tenureMonths)).setScale(2, RoundingMode.HALF_UP);
    BigDecimal totalInterest = totalPayment.subtract(principal).setScale(2, RoundingMode.HALF_UP);

    EmiCalculatorData data = new EmiCalculatorData();
    data.setEmi(emi);
    data.setTotalPayment(totalPayment);
    data.setTotalInterest(totalInterest);

    return BaseResponseUtil.emiSuccess("EMI calculated successfully", data);
  }

  public SipCalculatorResponse calculateSip(SipCalculatorRequest request) {
    BigDecimal monthly = request.getMonthlyInvestment();
    double annualRate = request.getAnnualReturnRate().doubleValue();
    int months = request.getTenureYears() * 12;

    // FV = P * ((1+r)^n - 1) / r * (1+r)
    double r = annualRate / 1200;
    double fv = monthly.doubleValue() * (Math.pow(1 + r, months) - 1) / r * (1 + r);

    BigDecimal futureValue = BigDecimal.valueOf(fv).setScale(2, RoundingMode.HALF_UP);
    BigDecimal totalInvested =
        monthly.multiply(BigDecimal.valueOf(months)).setScale(2, RoundingMode.HALF_UP);
    BigDecimal wealthGained = futureValue.subtract(totalInvested).setScale(2, RoundingMode.HALF_UP);

    SipCalculatorData data = new SipCalculatorData();
    data.setTotalInvested(totalInvested);
    data.setFutureValue(futureValue);
    data.setWealthGained(wealthGained);

    return BaseResponseUtil.sipSuccess("SIP calculated successfully", data);
  }

  public LumpsumCalculatorResponse calculateLumpsum(LumpsumCalculatorRequest request) {
    BigDecimal amount = request.getInvestmentAmount();
    double annualRate = request.getAnnualReturnRate().doubleValue();
    int years = request.getTenureYears();

    // FV = P * (1 + r)^n
    double fv = amount.doubleValue() * Math.pow(1 + annualRate / 100, years);

    BigDecimal futureValue = BigDecimal.valueOf(fv).setScale(2, RoundingMode.HALF_UP);
    BigDecimal wealthGained = futureValue.subtract(amount).setScale(2, RoundingMode.HALF_UP);

    LumpsumCalculatorData data = new LumpsumCalculatorData();
    data.setTotalInvested(amount);
    data.setFutureValue(futureValue);
    data.setWealthGained(wealthGained);

    return BaseResponseUtil.lumpsumSuccess("Lumpsum calculated successfully", data);
  }
}
