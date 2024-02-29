package com.patika.Atm.controller;

import com.patika.Atm.dto.AccountDto;
import com.patika.Atm.dto.UpdateAccountDto;
import com.patika.Atm.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/{userId}")
    public ResponseEntity<AccountDto> createAccount(@PathVariable Long userId){
        return new ResponseEntity<>(accountService.createAccount(userId), HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/byUserId/{userId}")
    public ResponseEntity<List<AccountDto>> getActiveAccountsByUserId(@PathVariable Long userId){
        return new ResponseEntity<>(accountService.getActiveAccountsByUserId(userId), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id){
        return new ResponseEntity<>(accountService.getAccountById(id), HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable Long id, @RequestBody UpdateAccountDto updateAccountDto){
        return new ResponseEntity<>(accountService.updateAccount(id, updateAccountDto), HttpStatus.OK);
    }

}
