package pl.banksystem.logic.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.banksystem.logic.account.transaction.Transaction;
import pl.banksystem.logic.account.transaction.TransactionStatus;
import pl.banksystem.logic.account.transaction.tracking.TransactionStatusType;
import pl.banksystem.logic.service.TransactionService;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/all")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactionList = transactionService.getAll();
        return transactionList.isEmpty() ? ResponseEntity.status(NO_CONTENT).build() : ResponseEntity.ok(transactionList);
    }

    @GetMapping("/account/{accountID}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountID(@PathVariable(name = "accountID") Long accountID) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountID(accountID);
        return transactions.isEmpty() ? ResponseEntity.status(NO_CONTENT).build() : ResponseEntity.ok(transactions);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Transaction> findTransactionByTransactionID(@PathVariable(name = "id") Long transactionID) {
        Optional<Transaction> transaction = transactionService.findTransactionByTransactionID(transactionID);
        return transaction.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(NO_CONTENT).build());
    }

    @GetMapping(value = "/{id}/status")
    public ResponseEntity<List<TransactionStatus>> getStatusByID(@PathVariable("id") Long transactionID) {
        List<TransactionStatus> statusByID = transactionService.getStatusByID(transactionID);
        return statusByID.isEmpty() ? ResponseEntity.status(NO_CONTENT).build() : ResponseEntity.ok(statusByID);
    }

    @PostMapping("save")
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction t) {
        Transaction transaction = transactionService.addTransaction(t);
        return ResponseEntity.ok(transaction);
    }

    @PutMapping({"/{id}/updateStatus"})
    public ResponseEntity<String> updateTransactionStatus(@PathVariable(name = "id") Long transactionID, @RequestBody TransactionStatusType newStatus) {
        return transactionService.updateTransactionStatus(transactionID, newStatus);
    }


}
