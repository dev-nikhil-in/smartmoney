package com.nivtron.smartmoney.repository;

import com.nivtron.smartmoney.entity.EntityUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<EntityUser, Long> {

  @Query("SELECT u FROM EntityUser u WHERE u.email = :email AND u.isDeleted = false")
  Optional<EntityUser> findByEmail(String email);

  @Query(
      "SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM EntityUser u WHERE u.email = :email AND u.isDeleted = false")
  boolean existsByEmail(String email);
}
