package com.patika.Atm.repository;

import com.patika.Atm.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByUserIdAndIsActiveTrue(Long userId);
}
