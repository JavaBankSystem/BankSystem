package pl.banksystem.logic.account.transaction.tracking;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public enum TransactionStatusType {

    PENDING(), CANCELED(new CanceledProcess(), new ArrayList<>(List.of(PENDING))), DONE(new DoneProcess(), new ArrayList<>(List.of(PENDING)));
    public static final Logger logger = LoggerFactory.getLogger(TransactionStatusType.class);
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

    public Transaction addTransaction(int accountID, double amount, TransactionType transactionType) {
        return new Transaction(accountID, amount, transactionType, new ArrayList<>(List.of(new TransactionStatus(PENDING, new Date()))));
    }


    public Transaction proccessTransaction(Transaction transaction) {
        List<TransactionStatus> transactionHistory = transaction.getTransactionHistory();
        TransactionStatusType previous = transactionHistory.get(transactionHistory.size() - 1).getTransactionStatusType();
        if (this.getAcceptedStatus()
                .contains(previous) && !(previous.equals(DONE) || previous.equals(CANCELED))) {
            return getStatusProcess().changeStatus(transaction);
        } else {
            logger.error("Change status problem!! Last status: {}, new TransactionStatusType: {}",
                    transaction.getActualTransactionStatus().getTransactionStatusType(), this);
            return null;
        }
    }
}
