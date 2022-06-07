package pl.banksystem.logic.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.banksystem.logic.account.Account;
import pl.banksystem.logic.account.loans.Loan;
import pl.banksystem.logic.account.loans.NewLoanForm;
import pl.banksystem.logic.service.AccountService;
import pl.banksystem.logic.service.LoanService;
import pl.banksystem.logic.service.UserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final AccountService accountService;
    private final UserService userService;

    @PostMapping("/loans")
    public ResponseEntity<String> createLoan(@RequestBody NewLoanForm loanForm) {

        Optional<Account> account = accountService.getAccountByAccountID(loanForm.getAccountId());
        if (account.isEmpty()) {
            log.error("Account with given id {} doesn't exist in the database", loanForm.getAccountId());
            return ResponseEntity.status(404).build();
        }
        log.info("Creating loan for user {}", account.get().getClient().getUsername());
        Loan loan = new Loan(loanForm.getInstallmentPercentage(),
                loanForm.getInstallmentPercentage(), loanForm.getNumberOfMonths(), loanForm.getLoanAmount());
        account.get().getCurrentLoans().add(loan);

        loanService.saveLoan(loan);
        return ResponseEntity.status(200).body("Loan successfully created");
    }

    @GetMapping("/loans/{username}/all")
    public ResponseEntity<List<Loan>> getAllUserLoans(@PathVariable String username) {
        List<Loan> allUserLoans = loanService.getAllUserLoans(username);
        log.info("Getting {}'s loans. Number of loans: {}", username, allUserLoans.size());
        return ResponseEntity.ok().body(allUserLoans);
    }

    @GetMapping("/loans/{loanId}")
    public ResponseEntity<Loan> getOneLoan(@PathVariable Long loanId) {
        Loan loan = loanService.getLoan(loanId);
        log.info("Getting loan with id {}", loanId);
        return ResponseEntity.ok().body(loan);
    }

    @PostMapping("/loans/pay")
    public ResponseEntity<String> payLoan(@RequestBody PayLoanForm payLoanForm) {
        if (loanService.getLoan(payLoanForm.getLoanId()) == null) {
            log.error("Couldn't find loan with given id: {}", payLoanForm.getLoanId());
            return ResponseEntity.status(404).body(
                    String.format("Couldn't find loan with given id: %d", payLoanForm.getLoanId()));
        }
        loanService.payLoan(payLoanForm.getLoanId(), payLoanForm.getAmount());
        return ResponseEntity.status(200).body("Loan pay succesfully");
    }
}

@Getter
class PayLoanForm {
    private final Long loanId;
    private final double amount;

    public PayLoanForm(Long loanId, double amount) {
        this.loanId = loanId;
        this.amount = amount;
    }
}
