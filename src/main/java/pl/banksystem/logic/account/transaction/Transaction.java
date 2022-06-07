package pl.banksystem.logic.account.transaction;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionID;
    public TransactionType transactionType;
    @OneToMany
    public List<TransactionStatus> transactionHistory;
    private double amount;
    private Long receiverAccountID;
    private Long senderAccountID;
    private Long accountID;
    private Long secondAccountID;
    private Date transactionDate = new Date();

    public Transaction(Long receiverAccountID, double amount, TransactionType transactionType) {
        this.receiverAccountID = receiverAccountID;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public Transaction(Long accountID, Long secondAccountID, double amount, TransactionType transactionType) {
        this.accountID = accountID;
        this.secondAccountID = secondAccountID;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public Transaction(Long receiverAccountID, double amount, TransactionType transactionType, List<TransactionStatus> transactionHistory) {
        this.receiverAccountID = receiverAccountID;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionHistory = transactionHistory;
    }

    public Transaction(Long receiverAccountID, Long senderAccountID, double amount, TransactionType transactionType, List<TransactionStatus> transactionHistory) {
        this.receiverAccountID = receiverAccountID;
        this.senderAccountID = senderAccountID;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionHistory = transactionHistory;
    }


    public TransactionStatus getActualTransactionStatus() {
        return getTransactionHistory().get(getTransactionHistory().size() - 1);
    }
}
