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
                .filter(transaction -> Objects.equals(transaction.getReceiverAccountID(), ID))
                .collect(Collectors.toList());
    }

    public Optional<Account> getAccountByAccountID(Long accountID) {
        return accountRepository.findAccountByAccountID(accountID);
    }

    public Optional<Account> findAccountByUserID(Long clientID) {
        return accountRepository.findAll().stream().findAny().filter(account -> Objects.equals(account.getClient().getId(), clientID));
    }

    public ResponseEntity<String> makeTransaction(Long transactionID) {
        Optional<Transaction> transaction = transactionRepository.findTransactionByTransactionID(transactionID);
        if (transaction.isEmpty()) {
            log.info("Given Transaction ID: {} not exist in DataBase", transactionID);
            return ResponseEntity.status(404).body(String.format("Given Transaction ID: %s not exist in DataBase", transactionID));
        }
        Optional<Account> sender = Optional.empty();
        if(transaction.get().getSenderAccountID() != null){
             sender = accountRepository.findAccountByAccountID(transaction.get().getSenderAccountID());
            if (sender.isEmpty()) {
                log.info("AccountID: {} not exist in DataBase", transaction.get().getSenderAccountID());
                return ResponseEntity.status(404).body(String.format("Given AccountID: %s not exist in DataBase", transaction.get().getSenderAccountID()));
            }
        }
        Optional<Account> receiver = accountRepository.findAccountByAccountID(transaction.get().getReceiverAccountID());
        if (receiver.isEmpty()) {
            log.info("AccountID: {} not exist in DataBase", transaction.get().getReceiverAccountID());
            return ResponseEntity.status(404).body(String.format("Given AccountID: %s not exist in DataBase", transaction.get().getReceiverAccountID()));
        }
        TransactionStatusType status = transaction.get().getActualTransactionStatus().getTransactionStatusType();
        if (status != TransactionStatusType.DONE) {
            log.info("The transaction cannot be recorded. Current status: {}", status);
            return ResponseEntity.status(406).body(String.format("The transaction cannot be recorded. Current status: %s", status));
        }
        sender.ifPresent(account -> {
            account.transaction(transaction.get());
            accountRepository.save(account);
        });
        receiver.get().transaction(transaction.get());
        accountRepository.save(receiver.get());
        return ResponseEntity.ok().body(String.format("Transaction %s done", transaction.get().getTransactionID()));
    }

    public Account addAccount(Account account) {
        accountRepository.save(account);
        return account;
    }

}
