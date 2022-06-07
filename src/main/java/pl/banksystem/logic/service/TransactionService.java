package pl.banksystem.logic.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.banksystem.logic.account.transaction.Transaction;
import pl.banksystem.logic.account.transaction.TransactionStatus;
import pl.banksystem.logic.account.transaction.tracking.TransactionStatusType;
import pl.banksystem.logic.repository.TransactionRepository;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;


    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByAccountID(Long ID) {
        return transactionRepository.findAll()
                .stream()
                .filter(transaction -> Objects.equals(transaction.getReceiverAccountID(), ID))
                .collect(Collectors.toList());
    }

    public Optional<Transaction> findTransactionByTransactionID(Long transactionID) {
        return transactionRepository.findTransactionByTransactionID(transactionID);
    }

    public List<TransactionStatus> getStatusByID(Long id) {
        return transactionRepository.findById(id).get().transactionHistory;
    }

    public Transaction addTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
        return transaction;
    }

    public ResponseEntity<String> updateTransactionStatus(Long transactionID, TransactionStatusType newStatus) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionID);
        if (optionalTransaction.isEmpty()) {
            log.info("Given Transaction ID: {} not exist in DataBase", transactionID);
            return ResponseEntity.status(404).body(String.format("Given Transaction ID: %s not exist in DataBase", transactionID));
        }
        Transaction transaction = optionalTransaction.get();

        Transaction updatedTransaction = newStatus.proccessTransaction(transaction);
        if (updatedTransaction == null) {
            return ResponseEntity.status(409)
                    .body(String.format("Change status problem!! Last status: %s, new TransactionStatusType: %s",
                            transaction.getActualTransactionStatus().getTransactionStatusType(), newStatus));
        }
        transactionRepository.save(updatedTransaction);
        return ResponseEntity.ok(String.format("Transaction %s status updated to: %s",
                updatedTransaction.getTransactionID(), newStatus));
    }

}
