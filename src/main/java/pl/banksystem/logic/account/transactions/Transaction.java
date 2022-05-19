package pl.banksystem.logic.account.transactions;

import lombok.Getter;
import org.springframework.data.annotation.Id;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;
import java.util.List;

@Getter

public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionID;
    public TransactionType transactionType;
    public List<TransactionStatus> transactionHistory;
    private final double amount;
    private final int accountID;
    private final Date transactionDate = new Date();

    public Transaction(int accountID, double amount, TransactionType transactionType) {
        this.accountID = accountID;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public Transaction(int accountID, double amount, TransactionType transactionType, List<TransactionStatus> transactionHistory) {
        this.accountID = accountID;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionHistory = transactionHistory;
    }

    public TransactionStatus getActuallTransactionStatus() {
        return getTransactionHistory().get(getTransactionHistory().size() - 1);
    }
}
