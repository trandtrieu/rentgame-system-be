package com.service;

import com.dto.AccountDTO;
import com.model.Account;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Account getAccount(Long accountId);

    List<AccountDTO> getListAccount();

    List<AccountDTO> getTop3AccountsByBalance();

    AccountDTO getAccountById(Long accountId);

    AccountDTO updateAccount(Long accountId, AccountDTO accountDTO);

    Optional<Account> findAccountById(Long accountId);

    void updateAccountBalance(Long accountId, double amount);

    @Transactional
    void saveOrUpdate(Account account);
}
