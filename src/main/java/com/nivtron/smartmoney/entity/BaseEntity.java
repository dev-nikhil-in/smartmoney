package com.nivtron.smartmoney.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Getter
@Setter
@MappedSuperclass
@Where(clause = "is_deleted = false")
public abstract class BaseEntity {

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column private LocalDateTime updatedAt;

  @Column(nullable = false)
  private Boolean isDeleted = false;

  @Column private LocalDateTime deletedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    isDeleted = false;
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  public void softDelete() {
    this.isDeleted = true;
    this.deletedAt = LocalDateTime.now();
  }
}
