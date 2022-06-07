package pl.banksystem.logic.account.transaction;

import lombok.*;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor

@Entity
@Table(name = "Transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionID;
    public TransactionType transactionType;
    @OneToMany
    public List<TransactionStatus> transactionHistory;
    private double amount;
    private Long accountID;
    private Long secondAccountID;
    private Date transactionDate = new Date();

    public Transaction(Long accountID, double amount, TransactionType transactionType) {
        this.accountID = accountID;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public Transaction(Long accountID, Long secondAccountID, double amount, TransactionType transactionType) {
        this.accountID = accountID;
        this.secondAccountID = secondAccountID;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public Transaction(Long accountID, double amount, TransactionType transactionType, List<TransactionStatus> transactionHistory) {
        this.accountID = accountID;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionHistory = transactionHistory;
    }
    public TransactionStatus getActualTransactionStatus() {
        return getTransactionHistory().get(getTransactionHistory().size() - 1);
    }
}
