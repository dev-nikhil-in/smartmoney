package com.nivtron.smartmoney.repository;

import com.nivtron.smartmoney.entity.EntityLoan;
import com.nivtron.smartmoney.entity.EntityUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<EntityLoan, Long> {
  List<EntityLoan> findAllByUser(EntityUser user);

  Optional<EntityLoan> findByIdAndUser(Long id, EntityUser user);
}
