package com.controller;

import com.dto.AccountDTO;
import com.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/rent-game/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<String> updateAcountU(@PathVariable Long id, @RequestBody AccountDTO accountDTO ){
        accountService.updateAccountu(id, accountDTO);
        return ResponseEntity.ok("Account updated");
    }

    @PutMapping(path = "/img/{id}")
    public ResponseEntity<String> updateAccountImage(@PathVariable Long id, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        accountService.updateAccountImage(id, multipartFile);
        return ResponseEntity.ok("Image Updated");
    }

    @GetMapping(path = "/top3")
    public ResponseEntity<List<AccountDTO>> getTop3Account_Balance() {
        return ResponseEntity.ok(accountService.getTop3AccountsByBalance());
    }
}
