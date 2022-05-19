package pl.banksystem.logic.account;


import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import pl.banksystem.logic.account.transaction.Transaction;
import pl.banksystem.logic.account.transaction.tracking.TransactionStatusType;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Getter

public class Account {
    public static final Logger logger = LoggerFactory.getLogger(Account.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private final int clientID;

    public Account(int clientID) {
        this.clientID = clientID;
    }

    public Account(int clientID, double balance) {
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
