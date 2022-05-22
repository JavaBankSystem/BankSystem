package pl.banksystem.logic.account;


import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.banksystem.logic.account.transaction.Transaction;
import pl.banksystem.logic.account.transaction.tracking.TransactionStatusType;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor

@Entity
@Table(name = "Account")

public class Account {
    public static final Logger logger = LoggerFactory.getLogger(Account.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountID;
    private Long clientID;

    public Account(Long clientID) {
        this.clientID = clientID;
    }

    public Account(Long clientID, double balance) {
        this.clientID = clientID;
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
            }
        } else {
            logger.warn("The transaction cannot be recorded. Current status: {}"
                    , transaction.getActualTransactionStatus().getTransactionStatusType());
        }

    }
}
