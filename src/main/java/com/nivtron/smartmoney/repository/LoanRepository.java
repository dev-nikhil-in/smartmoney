package com.nivtron.smartmoney.repository;

import com.nivtron.smartmoney.entity.EntityLoan;
import com.nivtron.smartmoney.entity.EntityUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<EntityLoan, Long> {

  @Query("SELECT l FROM EntityLoan l WHERE l.user = :user AND l.isDeleted = false")
  List<EntityLoan> findAllByUser(EntityUser user);

  @Query("SELECT l FROM EntityLoan l WHERE l.id = :id AND l.user = :user AND l.isDeleted = false")
  Optional<EntityLoan> findByIdAndUser(Long id, EntityUser user);
}
