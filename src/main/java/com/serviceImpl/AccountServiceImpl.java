package com.serviceImpl;

import com.dto.AccountDTO;
import com.model.Account;
import com.repository.AccountRepository;
import com.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    private AccountDTO mapToDTO(Account account) {
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
        accountDTO.setBalance(account.getBalance());
        return accountDTO;
    }
    private Account mapToEntity(AccountDTO accountDTO) {
        Account account = new Account();
        account.setId(accountDTO.getId());
        account.setName(accountDTO.getName());
        account.setMail(accountDTO.getMail());
        account.setUsername(accountDTO.getUsername());
        account.setDob(accountDTO.getDob());
        account.setPassword(accountDTO.getPassword());
        account.setAvatar(accountDTO.getAvatar());
        account.setPhone(accountDTO.getPhone());
        account.setRoles(accountDTO.getRoles());
        account.setBalance(accountDTO.getBalance());
        return account;
    }
    @Override
    public Account getAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account with id: " + accountId + " not found!!"));
    }

    @Override
    public AccountDTO getAccountById(Long accountId) {
        Account account = getAccount(accountId);
        return mapToDTO(account);
    }

    @Override
    public List<AccountDTO> getListAccount() {
        List<Account> accountList = accountRepository.findAll();
        return accountList.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<AccountDTO> getTop3AccountsByBalance() {
        List<Account> accountList = accountRepository.findAll();
        return accountList.stream()
                .sorted(Comparator.comparing(Account::getBalance).reversed())
                .limit(3)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDTO updateAccount(Long accountId, AccountDTO accountDTO) {
        Account existingAccount = getAccount(accountId);
        existingAccount.setName(accountDTO.getName());
        existingAccount.setMail(accountDTO.getMail());
        existingAccount.setUsername(accountDTO.getUsername());
        existingAccount.setDob(accountDTO.getDob());
        existingAccount.setPassword(accountDTO.getPassword());
        existingAccount.setAvatar(accountDTO.getAvatar());
        existingAccount.setPhone(accountDTO.getPhone());
        existingAccount.setRoles(accountDTO.getRoles());
        existingAccount.setBalance(accountDTO.getBalance());

        Account updatedAccount = accountRepository.save(existingAccount);
        return mapToDTO(updatedAccount);
    }
}
