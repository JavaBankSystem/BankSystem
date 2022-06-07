package pl.banksystem.logic.account;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.banksystem.logic.account.loans.Loan;
import pl.banksystem.logic.account.loans.LoanStatus;
import pl.banksystem.logic.account.transaction.Transaction;
import pl.banksystem.logic.account.transaction.tracking.TransactionStatusType;
import pl.banksystem.logic.domain.AppUser;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "client"})
public class Account {
    public static final Logger logger = LoggerFactory.getLogger(Account.class);
    @Id
    @GenericGenerator(name = "IdGenerator",
            strategy = "pl.banksystem.logic.account.generators.IdGenerator")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IdGenerator")
    private Long accountID;

    @ManyToOne
    private AppUser client;

    @OneToMany
    private List<Loan> currentLoans = new ArrayList<>();

    @OneToMany
    private List<Loan> archiveLoans = new ArrayList<>();

    public Account(AppUser client) {
        this.client = client;
    }

    public Account(AppUser client, double balance) {
        this.client = client;
        this.balance = balance;
    }

    @Setter
    private double balance;

    public void transaction(Transaction transaction) {
        if (transaction.getActualTransactionStatus().getTransactionStatusType() == TransactionStatusType.DONE) {
            switch (transaction.getTransactionType()) {
                case Withdraw:
                    setBalance(getBalance() - transaction.getAmount());
                    logger.info("Current Operation: {} , Balance after transaction: {}", transaction.transactionType, getBalance());
                    break;

                case Deposit:
                    setBalance(getBalance() + transaction.getAmount());
                    logger.info("Current Operation: {} , Balance after transaction: {}", transaction.transactionType, getBalance());
                    break;

                case PayLoan:
                    setBalance(getBalance() - transaction.getAmount());
                    Optional<Loan> loan = currentLoans.stream().filter(l -> Objects.equals(l.getId(), transaction.getSecondAccountID())).findFirst();
                    if (loan.isEmpty()) {
                        logger.error("Couldn't find loan with given id: {}", transaction.getSecondAccountID());
                        return;
                    }
                    LoanStatus ls = loan.get().payLoan(transaction.getAmount());
                    if (ls.equals(LoanStatus.ENDED)) {
                        archiveLoans.add(loan.get());
                        currentLoans.remove(loan.get());
                    }
                    break;
                case Transfer:
                    if (Objects.equals(transaction.getSenderAccountID(), getAccountID())) {
                        setBalance(getBalance() - transaction.getAmount());
                        logger.info("Out-coming transfer , Balance after transfer: {}", getBalance());
                    } else {
                        setBalance(getBalance() + transaction.getAmount());
                        logger.info("Incoming transfer , Balance after transfer: {}", getBalance());
                    }
                    break;
            }
        } else {
            logger.warn("The transaction cannot be recorded. Current status: {}"
                    , transaction.getActualTransactionStatus().getTransactionStatusType());
        }

    }
}
