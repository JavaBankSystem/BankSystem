package pl.banksystem.logic.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.banksystem.logic.account.Account;
import pl.banksystem.logic.account.transaction.Transaction;
import pl.banksystem.logic.account.transaction.tracking.TransactionStatusType;
import pl.banksystem.logic.repository.AccountRepository;
import pl.banksystem.logic.repository.TransactionRepository;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public List<Transaction> getTransactionsByAccountID(Long ID) {
        return transactionRepository.findAll()
                .stream()
                .filter(transaction -> Objects.equals(transaction.getAccountID(), ID))
                .collect(Collectors.toList());
    }

    public Optional<Account> getAccountByAccountID(Long accountID) {
        return accountRepository.findAccountByAccountID(accountID);
    }

//    public Optional<Account> findAccountByClientId(Long clientId) {
//        return accountRepository.findAccountByClientID(clientId);
//    }

    public ResponseEntity<String> makeTransaction(Long accountID, Long transactionID) {
        Optional<Account> account = accountRepository.findAccountByAccountID(accountID);
        if (account.isEmpty()) {
            log.info("Given AccountID: {} not exist in DataBase", accountID);
            return ResponseEntity.status(404).body(String.format("Given AccountID: %s not exist in DataBase", accountID));
        }
        Optional<Transaction> transaction = transactionRepository.findTransactionByTransactionID(transactionID);
        if (transaction.isEmpty()) {
            log.info("Given Transaction ID: {} not exist in DataBase", transactionID);
            return ResponseEntity.status(404).body(String.format("Given Transaction ID: %s not exist in DataBase", transactionID));
        }
        if (!Objects.equals(transaction.get().getAccountID(), accountID)) {
            log.info("The transaction {} does not apply to the account number: {}", transactionID, accountID);
            return ResponseEntity.status(404).body(String.format("The transaction %s does not apply to the account number: %s", transactionID, accountID));
        }
        TransactionStatusType status = transaction.get().getActualTransactionStatus().getTransactionStatusType();
        if (status != TransactionStatusType.DONE) {
            log.info("The transaction cannot be recorded. Current status: {}", status);
            return ResponseEntity.status(406).body(String.format("The transaction cannot be recorded. Current status: %s", status));
        }
        account.get().transaction(transaction.get());
        accountRepository.save(account.get());
        return ResponseEntity.ok().body(String.format("Current Operation: %s , Balance after transaction: %s", transaction.get().transactionType, account.get().getBalance()));
    }

    public Account addAccount(Account account) {
        accountRepository.save(account);
        return account;
    }

}
