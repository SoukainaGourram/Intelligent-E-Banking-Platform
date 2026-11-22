package net.gourram.ebankservice.service;

import net.gourram.ebankservice.entities.BankAccount;
import net.gourram.ebankservice.feign.CustomerRestClients;
import net.gourram.ebankservice.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EbankService {
    public BankAccountRepository accountRepository;
    private CustomerRestClients customerRestClients;

    public EbankService(BankAccountRepository accountRepository, CustomerRestClients customerRestClients) {
        this.accountRepository = accountRepository;
        this.customerRestClients = customerRestClients;
    }

    public List<BankAccount> getAllBankAccount() {
        return accountRepository.findAll();
    }

    public BankAccount getBankAccountByID(String id) throws Throwable {
        BankAccount bankAccount = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        bankAccount.setCustomer(
                customerRestClients.getCustomerById(bankAccount.getCustomerId()));
        return bankAccount;
    }

    public BankAccount save(BankAccount bankAccount) {
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreatedAt(new Date());
        return accountRepository.save(bankAccount);
    }
}
