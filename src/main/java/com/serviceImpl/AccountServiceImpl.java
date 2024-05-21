package com.serviceImpl;

import com.dto.AccountDTO;
import com.model.Account;
import com.repository.AccountRepository;
import com.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account getAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account with id: " + accountId + " not found!!"));
    }

    @Override
    public AccountDTO getAccountById(Long accountId) {
        Account account = getAccount(accountId);
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setName(account.getName());
        accountDTO.setMail(account.getMail());
        accountDTO.setUsername(account.getUsername());
        accountDTO.setDob(account.getDob());
        accountDTO.setPassword(account.getPassword());
        accountDTO.setAvatar(account.getAvatar());
        accountDTO.setPhone(account.getPhone());
        accountDTO.setRoles(account.getRoles());

        return accountDTO;
    }

    @Override
    public List<AccountDTO> getListAccount() {
        List<Account> accountList = accountRepository.findAll();
        return accountList.stream().map(account -> {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setId(account.getId());
            accountDTO.setName(account.getName());
            accountDTO.setMail(account.getMail());
            accountDTO.setUsername(account.getUsername());
            accountDTO.setDob(account.getDob());
            accountDTO.setAvatar(account.getAvatar());
            accountDTO.setPhone(account.getPhone());
            accountDTO.setRoles(account.getRoles());
            accountDTO.setBalance(account.getBalance());
            return accountDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AccountDTO> getTop3AccountsByBalance() {
        List<Account> accountList = accountRepository.findAll();

        return accountList.stream()
                .sorted(Comparator.comparing(Account::getBalance).reversed())
                .limit(3)
                .map(account -> {
                    AccountDTO accountDTO = new AccountDTO();
                    accountDTO.setId(account.getId());
                    accountDTO.setName(account.getName());
                    accountDTO.setMail(account.getMail());
                    accountDTO.setUsername(account.getUsername());
                    accountDTO.setDob(account.getDob());
                    accountDTO.setAvatar(account.getAvatar());
                    accountDTO.setPhone(account.getPhone());
                    accountDTO.setRoles(account.getRoles());
                    accountDTO.setBalance(account.getBalance());
                    return accountDTO;
                })
                .collect(Collectors.toList());
    }
}
