package pl.banksystem.logic.account.transactions.tracking.processes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.banksystem.logic.account.transactions.Transaction;
import pl.banksystem.logic.account.transactions.TransactionStatus;
import pl.banksystem.logic.account.transactions.tracking.TransactionStatusType;

import java.util.Date;

import static pl.banksystem.logic.account.transactions.tracking.TransactionStatusType.PENDING;

public class PendingProcess implements StatusProcess {
    public static final Logger logger = LoggerFactory.getLogger(PendingProcess.class);

    @Override
    public Transaction changeStatus(Transaction transaction) {
        logger.info("Setting process status to PENDING");
        transaction.getTransactionHistory().add(new TransactionStatus(PENDING, new Date()));

        //TODO send message to client
        //Message.send("Twoj przelew przyjęty do realizacji" + dane...);
        return transaction;
    }
}
