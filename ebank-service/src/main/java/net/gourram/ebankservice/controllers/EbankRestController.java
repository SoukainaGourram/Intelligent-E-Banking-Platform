package net.gourram.ebankservice.controllers;

import net.gourram.ebankservice.entities.BankAccount;
import net.gourram.ebankservice.service.EbankService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EbankRestController {
    private EbankService ebankService;

    public EbankRestController(EbankService ebankService) {
        this.ebankService = ebankService;
    }

    @GetMapping("/accounts")
    public List<BankAccount> getAllBankAccount() {
        return ebankService.getAllBankAccount();
    }

    @GetMapping("/accounts/{id}")
    public BankAccount getBankAccountByID(@PathVariable String id) throws Throwable {
        return ebankService.getBankAccountByID(id);
    }

    @PostMapping("/accounts")
    public BankAccount save(@RequestBody BankAccount bankAccount) {
        return ebankService.save(bankAccount);
    }
}
