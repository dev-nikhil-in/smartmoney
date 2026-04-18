package com.nivtron.smartmoney.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "loans")
public class EntityLoan extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private EntityUser user;

  @Column(nullable = false, length = 100)
  private String loanName;

  @Column(nullable = false, precision = 15, scale = 2)
  private BigDecimal totalAmount;

  @Column(nullable = false, precision = 15, scale = 2)
  private BigDecimal outstandingAmount;

  @Column(nullable = false, precision = 5, scale = 2)
  private BigDecimal interestRate;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal emiAmount;

  @Column(nullable = false)
  private Integer tenureMonths;

  @Column(nullable = false)
  private LocalDate startDate;
}
