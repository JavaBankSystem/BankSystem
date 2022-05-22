package pl.banksystem.logic.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.banksystem.logic.account.Account;
import pl.banksystem.logic.account.transaction.Transaction;
import pl.banksystem.logic.service.AccountService;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAll();
        return accounts.isEmpty() ? ResponseEntity.status(NO_CONTENT).build() : ResponseEntity.ok(accounts);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Account> findAccountByAccountID(@PathVariable(name = "id") Long accountID) {
        Optional<Account> account = accountService.getAccountByAccountID(accountID);
        return account.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(NO_CONTENT).build());
    }

    @GetMapping(value = "/client/{id}")
    public ResponseEntity<Account> findAccountByClientId(@PathVariable(name = "id") Long clientId) {
        Optional<Account> account = accountService.findAccountByClientId(clientId);
        return account.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(NO_CONTENT).build());
    }

    @GetMapping(value = "/{id}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountID(@PathVariable(name = "id") Long accountID) {
        List<Transaction> transactions = accountService.getTransactionsByAccountID(accountID);
        return transactions.isEmpty() ? ResponseEntity.status(NO_CONTENT).build() : ResponseEntity.ok(transactions);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<Account> addAccount(@RequestBody Account account) {
        return ResponseEntity.ok(accountService.addAccount(account));
    }

    @PutMapping(value = "/{accountID}/{transactionID}")
    public ResponseEntity<String> makeTransaction(@PathVariable(name = "accountID") Long accountID,
                                                  @PathVariable(name = "transactionID") Long transactionID) {
        return accountService.makeTransaction(accountID, transactionID);
    }
}
