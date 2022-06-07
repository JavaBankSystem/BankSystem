package pl.banksystem.logic.account.transaction.tracking;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pl.banksystem.logic.account.transaction.Transaction;
import pl.banksystem.logic.account.transaction.TransactionStatus;
import pl.banksystem.logic.account.transaction.TransactionType;
import pl.banksystem.logic.account.transaction.tracking.processes.CanceledProcess;
import pl.banksystem.logic.account.transaction.tracking.processes.DoneProcess;
import pl.banksystem.logic.account.transaction.tracking.processes.StatusProcess;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Slf4j
public enum TransactionStatusType {

    PENDING(), CANCELED(new CanceledProcess(), new ArrayList<>(List.of(PENDING))), DONE(new DoneProcess(), new ArrayList<>(List.of(PENDING)));
    private final StatusProcess statusProcess;
    private final List<TransactionStatusType> acceptedStatus;

    TransactionStatusType(StatusProcess statusProcess, List<TransactionStatusType> acceptedStatus) {
        this.statusProcess = statusProcess;
        this.acceptedStatus = acceptedStatus;
    }

    TransactionStatusType() {
        this.statusProcess = null;
        this.acceptedStatus = null;
    }

    public Transaction addTransaction(Long accountID, double amount, TransactionType transactionType) {
        return new Transaction(accountID, amount, transactionType, new ArrayList<>(List.of(new TransactionStatus(PENDING, new Date()))));
    }

    public Transaction addTransaction(Long receiverAccountID, Long senderAccountID, double amount, TransactionType transactionType) {
        return new Transaction(receiverAccountID, senderAccountID, amount, transactionType, new ArrayList<>(List.of(new TransactionStatus(PENDING, new Date()))));
    }

    public Transaction proccessTransaction(Transaction transaction) {
        List<TransactionStatus> transactionHistory = transaction.getTransactionHistory();
        TransactionStatusType previous = transactionHistory.get(transactionHistory.size() - 1).getTransactionStatusType();
        if (this.getAcceptedStatus()
                .contains(previous) && !(previous.equals(DONE) || previous.equals(CANCELED))) {
            return getStatusProcess().changeStatus(transaction);
        } else {
            log.error("Change status problem!! Last status: {}, new TransactionStatusType: {}",
                    transaction.getActualTransactionStatus().getTransactionStatusType(), this);
            return null;
        }
    }
}
