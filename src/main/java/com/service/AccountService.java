package com.service;

import com.dto.AccountDTO;
import com.model.Account;

import java.util.List;

public interface AccountService {
    Account getAccount(Long accountId);

    List<AccountDTO> getListAccount();

    List<AccountDTO> getTop3AccountsByBalance();

    AccountDTO getAccountById(Long accountId);


}
