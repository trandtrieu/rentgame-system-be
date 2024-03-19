package com.serviceImpl;

import com.dto.AccountDTO;
import com.model.Account;
import com.repository.AccountRepository;
import com.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    private final String FOLDER_PATH = "D:/Documents/OJT/mock-project/pharmacy-online-fe/public/assets/images";
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
        accountDTO.setAddress(account.getAddress());
        return accountDTO;
    }

    @Override
    public void updateAccountu(Long id, AccountDTO accountDTO) {
        Account accountToUpdate = getAccount(id);

        // Update account fields from the DTO
        accountToUpdate.setName(accountDTO.getName());
        accountToUpdate.setMail(accountDTO.getMail());
        accountToUpdate.setAddress(accountDTO.getAddress());
//        accountToUpdate.setUsername(accountDTO.getUsername());
        accountToUpdate.setDob(accountDTO.getDob());
//        accountToUpdate.setPassword(accountDTO.getPassword());
        accountToUpdate.setAvatar(accountDTO.getAvatar());
        accountToUpdate.setPhone(accountDTO.getPhone());
//        accountToUpdate.setRoles(accountDTO.getRoles());
        accountToUpdate.setAccount_balance(accountDTO.getAccount_balance());

        // Save the updated account
        accountRepository.save(accountToUpdate);
    }


    @Override
    public void updateAccountImage(Long accountId, MultipartFile multipartFile) throws IOException {
        String filePath = FOLDER_PATH + multipartFile.getOriginalFilename();
        Account account = getAccount(accountId);
        account.setAvatar(multipartFile.getOriginalFilename());
        multipartFile.transferTo(new File(filePath));
        accountRepository.save(account);
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
            accountDTO.setAccount_balance(account.getAccount_balance());
            return accountDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AccountDTO> getTop3AccountsByBalance() {
        List<Account> accountList = accountRepository.findAll();

        return accountList.stream()
                .sorted(Comparator.comparing(Account::getAccount_balance).reversed())
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
                    accountDTO.setAccount_balance(account.getAccount_balance());
                    return accountDTO;
                })
                .collect(Collectors.toList());
    }
}
