package com.service;

import com.dto.AccountDTO;
import com.model.Account;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AccountService {
    Account getAccount(Long accountId);

    List<AccountDTO> getListAccount();

    List<AccountDTO> getTop3AccountsByBalance();

    AccountDTO getAccountById(Long accountId);

    public void updateAccountu(Long accountId, AccountDTO accountDTO);

    public void updateAccountImage(Long accountId, MultipartFile multipartFile) throws IOException;
}
